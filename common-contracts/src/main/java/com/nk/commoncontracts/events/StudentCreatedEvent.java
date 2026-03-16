package com.nk.commoncontracts.events;

import lombok.Data;

@Data
public class StudentCreatedEvent {
    private String studentId;
    private String collegeId;
    private String deptId;
}
