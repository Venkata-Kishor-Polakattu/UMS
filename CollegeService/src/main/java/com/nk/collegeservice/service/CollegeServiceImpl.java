package com.nk.collegeservice.service;

import com.nk.collegeservice.beans.College;
import com.nk.collegeservice.repo.CollegeRepository;
import com.nk.commoncontracts.events.StudentCreatedEvent;
import com.nk.commoncontracts.respnse.CollegeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class CollegeServiceImpl implements CollegeService{

    private final CollegeRepository collegeRepository;

    @Override
    public String createClg(College clg) {
       return collegeRepository.createCollege(clg);
    }

    public CollegeResponseDto checkCollege(String cid){
        College college = collegeRepository.checkCollege(cid);
        CollegeResponseDto response=new CollegeResponseDto();
        if (college==null){
            response.setValid(false);
            response.setCollegeName("Nothing");
            return  response;
        }else {
            response.setValid(true);
            response.setCollegeName(college.getName());
            return response;
        }
    }

    @Override
    public College getCollege(String id) {
        return collegeRepository.findCollege(id);
    }

    @Override
    public String updateDeptCount(College college){
        return collegeRepository.updateDeptCount(college);
    }

    @Override
    public void updateStudentsCount(StudentCreatedEvent event){
        collegeRepository.updateStudentsCount(event);
    }

    @Override
    public String updateClg(College clg) {
        String res = collegeRepository.updateCollege(clg);

        if (!res.equals("success"))
            return res;

        /*String deptRes = departmentService.updateReference(clg.getId(),clg.getName());
        String studRes=studentService.updateClgReference(clg.getId(),clg.getName());
        return "College updated successfully, "+deptRes+" and "+studRes;*/
        return "College updated successfully";
    }

    @Override
    public String updateName(String cid,String clgName){
        String res = collegeRepository.updateClgName(cid, clgName);
        if (!res.equals("success"))
            return res;
        /*String deptRes = departmentService.updateReference(cid,clgName);
        String studRes=studentService.updateClgReference(cid,clgName);
        return "College updated successfully, "+deptRes+" and "+studRes;*/
        return "Name updated successfully";
    }

    @Override
    public String deleteClg(String cid) {
        return collegeRepository.deleteCollege(cid);
    }
}
