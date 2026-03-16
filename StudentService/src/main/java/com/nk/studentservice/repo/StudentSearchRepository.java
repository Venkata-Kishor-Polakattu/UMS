package com.nk.studentservice.repo;

import com.nk.studentservice.searchDocuments.StudentSearchDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface StudentSearchRepository extends ElasticsearchRepository<StudentSearchDocument,String> {

    List<StudentSearchDocument> findByCollegeName(String collegeName);

    List<StudentSearchDocument> findByDepartmentName(String departmentName);

    List<StudentSearchDocument> findByAgeGreaterThan(Integer ageIsGreaterThan);

    List<StudentSearchDocument> findByAgeLessThanEqual(Integer ageIsLessThan);

    List<StudentSearchDocument> findByYearEquals(Integer year);

    List<StudentSearchDocument> findByName(String name);
}
