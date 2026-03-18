package com.nk.studentservice.searchDocuments;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Builder
@Data
@Document(indexName = "students")
public class StudentSearchDocument {
    @Id
    private String id;
    private String departmentName;
    private String collegeName;
    private String name;
    private Integer year;
    private Integer age;
}
