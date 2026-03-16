package com.nk.commoncontracts.events;

import com.nk.commoncontracts.util.DepartmentSuggestion;
import lombok.Data;
import org.bson.Document;

import java.util.List;

@Data
public class DepartmentValidationFailedEvent {
    private String studentId;
    private String collegeId;
    private String deptId;
    private String message;
    private List<DepartmentSuggestion> availableDepartments;
}
