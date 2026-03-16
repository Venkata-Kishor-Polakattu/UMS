package com.nk.collegeservice.beans;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "colleges")
@Data
public class College {
    @Id
    private String id;
    private String name;
    private String location;
    private Integer yearOfEstablishment;
    private Integer departmentsCount;
    private Integer studentsCount;
}