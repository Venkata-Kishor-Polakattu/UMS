package com.nk.departmentservice.service;


import com.nk.commoncontracts.events.DepartmentValidationFailedEvent;
import com.nk.commoncontracts.events.StudentCreatedEvent;
import com.nk.commoncontracts.util.DepartmentSuggestion;
import com.nk.departmentservice.beans.Department;
import com.nk.departmentservice.messaging.producer.DepartmentEventProducer;
import com.nk.departmentservice.repo.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl {

    private final DepartmentEventProducer departmentEventProducer;

    private final DepartmentRepository repository;

    public Department getDept(String id){
        return repository.getDept(id);
    }

    public List<Department> getAll(){return repository.getAll();}

    public List<Department> getAllDeptByClgId(String cid){return repository.getAllDeptByCollege(cid);}

    public String createDept(Department department){
        if (repository.isPresent(department.getId())) return "Department already exists";

        /*College college=null;
        try {
            college=restTemplate.getForObject(clgUrl + department.getCollegeId(), College.class);
        }catch (Exception e){
            return "College does not exist with the code:"+department.getCollegeId();
        }

        college.setDepartmentsCount(college.getDepartmentsCount()+1);

        restTemplate.postForEntity(clgUrl+"editDeptCount",college,College.class);*/

        return repository.createDept(department);
    }

    public String editDept(Department dept){
        return repository.updateDept(dept);
    }

    public String updateReference(String clgId,String clgName){
       return repository.updateReference(clgId,clgName);
    }

    public String updateName(String did,String name){
        String res=repository.updateName(did, name);

        if (!res.equals("success"))
            return res;

        //String stdRes = studentService.updateDeptReference(did, name);

        return res+" and ";
    }

    public String deleteDept(String cid){
        return repository.deleteDept(cid);
    }

    public void increaseStudentCount(StudentCreatedEvent event){
        Department department=repository.getDept(event.getDeptId());
        if (department!=null){

            if (department.getCollegeId().equals(event.getCollegeId())){
                repository.increaseStudentCount(event.getDeptId());
            }else {
                DepartmentValidationFailedEvent failedEvent=new DepartmentValidationFailedEvent();
                failedEvent.setStudentId(event.getStudentId());
                failedEvent.setCollegeId(event.getCollegeId());

                List<Department> departments = repository.getAllDeptByCollege(event.getCollegeId());
                List<DepartmentSuggestion> suggestionList=new LinkedList<>();

                for (Department dept:departments){
                    DepartmentSuggestion suggestion=new DepartmentSuggestion();
                    suggestion.setId(dept.getId());
                    suggestion.setName(dept.getName());
                    suggestionList.add(suggestion);
                }

                if (suggestionList.isEmpty()){
                    failedEvent.setMessage("No department exist in the college "+event.getCollegeId()+" please check it once");
                }else {
                    failedEvent.setMessage("The department with id"+event.getDeptId()+" does not exist given college "+event.getCollegeId()+" refer the suggested departments");
                }

                failedEvent.setAvailableDepartments(suggestionList);

                departmentEventProducer.sendDepartmentValidationFailedEvent(failedEvent);
            }
        }
    }
}
