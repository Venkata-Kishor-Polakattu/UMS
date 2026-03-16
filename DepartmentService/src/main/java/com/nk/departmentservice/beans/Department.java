package com.nk.departmentservice.beans;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "departments")
@Data
public class Department {
    @Id
    private String id;
    //@Field("c_id")
    private String collegeId;
    private String collegeName;
    private String name;
    private String hod;
    private Integer studentsCount;
}