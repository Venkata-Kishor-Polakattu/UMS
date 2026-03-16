package com.nk.departmentservice.repo;


import com.mongodb.client.result.UpdateResult;
import com.nk.departmentservice.beans.Department;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class DepartmentRepository {
    private final MongoTemplate mongoTemplate;


    public void increaseStudentCount(String departmentId){
        Query query=Query.query(Criteria.where("_id").is(departmentId));

        Update update=new Update();
        update.inc("studentsCount",1);

        mongoTemplate.updateFirst(query,update,Department.class);
    }


    public String createDept(Department dept){
        mongoTemplate.save(dept);
        return "Department created successfully";
    }


    public String updateDept(Department dept ){
        Department foundDept=mongoTemplate.findById(dept.getId(), Department.class,"departments");
        if (foundDept==null) return "No department exist with id"+dept.getId();

        if (foundDept.getStudentsCount()!=null && !foundDept.getStudentsCount().equals(dept.getStudentsCount())) return "You cannot increase or decrease students count in dept";

        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(dept.getId()));

        Update update=new Update();
        update.set("c_id",dept.getCollegeId());
        update.set("name",dept.getName());
        update.set("HOD",dept.getHod());

        mongoTemplate.updateFirst(query,update,"departments");
        return "Department updated successfully";
    }

    public String updateName(String did,String name){
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(did));

        Update update=new Update();
        update.set("name",name);

        //mongoTemplate.updateFirst(query,update,College.class);
        return "success";
    }
    public String updateReference(String clgId,String clgName){
        Query query=new Query();
        Criteria criteria=Criteria.where("c_id").is(clgId);

        Update update=new Update();
        update.set("c_name",clgName);

        query.addCriteria(criteria);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Department.class);
        return updateResult.getModifiedCount()+" departments are updated";
    }

    public String deleteDept(String did){
        Department dept=mongoTemplate.findById(did,Department.class,"departments");
        if(dept==null) return "No Department exist with id :"+did;

        if (dept.getStudentsCount()>0){
            return "Still "+dept.getStudentsCount()+" students exist in "+dept.getName()+", unable to delete";
        }

        mongoTemplate.remove(dept,"departments");

        return "Department deleted successfully";
    }

    public List<Department> getAllDeptByCollege(String cid){

        Query query=new Query();
        query.addCriteria(Criteria.where("c_id").is(cid));

        return mongoTemplate.find(query, Department.class, "departments");
    }

    public Department getDeptByName(String deptName)throws Exception{
        Query query=new Query();
        query.addCriteria(Criteria.where("name").is(deptName));
        Department department=null;

        try {
            department=mongoTemplate.findOne(query,Department.class,"departments");
        }catch (Exception e){
            throw new Exception(deptName+" department does not exist");
        }
        return department;
    }

    public Department getDept(String id){
        return mongoTemplate.findById(id, Department.class, "departments");
    }

    public boolean exists(String departmentId){
        return mongoTemplate.findById(departmentId,Department.class)!=null;
    }

    public Boolean isPresent(String id){
        return mongoTemplate.findById(id, Department.class) != null;
    }

    public List<Department> getAll(){
        return mongoTemplate.findAll(Department.class,"departments");
    }
}
