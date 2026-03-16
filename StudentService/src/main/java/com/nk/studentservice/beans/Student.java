package com.nk.studentservice.beans;

import com.nk.studentservice.util.Hobby;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "students")
@Data
public class Student {
    @Id
    private String id;
    private String departmentId;
    private String departmentName;
    private String collegeId;
    private String collegeName;
    private String name;
    private Integer year;
    private Integer age;

    private List<Hobby> hobbies;

    private List<Language> languages;

    private List<Address> address;
}
