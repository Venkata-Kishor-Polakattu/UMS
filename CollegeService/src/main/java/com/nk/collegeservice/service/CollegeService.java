package com.nk.collegeservice.service;

import com.nk.collegeservice.beans.College;
import com.nk.commoncontracts.events.StudentCreatedEvent;
import com.nk.commoncontracts.respnse.CollegeResponseDto;

public interface CollegeService {
    public String createClg(College clg);
    public College getCollege(String id);
    public String updateClg(College clg);
    public String updateDeptCount(College college);
    public void updateStudentsCount(StudentCreatedEvent event);
    public String deleteClg(String cid);
    public String updateName(String cid,String clgName);
    public CollegeResponseDto checkCollege(String cid);
}
