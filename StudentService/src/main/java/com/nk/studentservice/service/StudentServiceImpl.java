package com.nk.studentservice.service;

import com.nk.studentservice.beans.Language;
import com.nk.studentservice.beans.Student;
import com.nk.commoncontracts.events.StudentCreatedEvent;
import com.nk.commoncontracts.respnse.CollegeResponseDto;
import com.nk.studentservice.client.CollegeClient;
import com.nk.studentservice.filters.DeptYearFilter;
import com.nk.studentservice.messaging.producer.StudentEventProducer;
import com.nk.studentservice.repo.StudentRepository;
import com.nk.studentservice.repo.StudentSearchRepository;
import com.nk.studentservice.searchDocuments.StudentSearchDocument;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl {

    @Value("${student.age}")
    private Integer minAge;

    private final StudentSearchRepository searchRepository;

    private final StudentEventProducer studentEventProducer;

    private final CollegeClient collegeClient;

    private final StudentRepository repository;

    public String createStudent(Student student){

        if (student.getAge()<minAge) return "Minimum age to join college is "+minAge;

        CollegeResponseDto college = collegeClient.checkCollege(student.getCollegeId());

        if (!college.getValid()){
            return "College with "+student.getCollegeId()+" does not exist";
        }

        student.setCollegeName(college.getCollegeName());

        String msg=repository.createStudent(student);

        StudentSearchDocument document=new StudentSearchDocument();
        document.setId(student.getId());
        document.setName(student.getName());
        document.setAge(student.getAge());
        document.setYear(student.getYear());
        document.setCollegeName(student.getCollegeName());
        document.setDepartmentName(student.getDepartmentName());

        searchRepository.save(document);


        StudentCreatedEvent event=new StudentCreatedEvent();
        event.setStudentId(student.getId());
        event.setCollegeId(student.getCollegeId());
        event.setDeptId(student.getDepartmentId());

        studentEventProducer.publishStudentCreatedEvent(event);

        return msg;
    }

    //aggregations

    public List<Document> maxLevelOfEachLanguage(){return repository.maxLevelOfEachLanguage();}

    public List<Document> multilingualStudents(){return repository.multilingualStudents();}

    public List<Document> topLanguage(){return repository.topLanguage();}

    public List<Document> avgLevelPerLanguage(){return repository.avgLevelPerLanguage();}
    public List<Document> studentsPerLanguage(){return repository.studentsPerLanguage();}

    public String updateClgReference(String clgId,String clgName){
       return repository.updateClgReference(clgId,clgName);
    }

    public String updateDeptReference(String did,String name){
        return repository.updateDeptReference(did,name);
    }

    public List<Student> byDeptIdNYearComb(List<DeptYearFilter> deptYearFilterList){
        return repository.byDeptIdNYearComb(deptYearFilterList);
    }

    public String increaseLanguagesLevel(List<String> languages){return repository.increaseLanguagesLevel(languages);}

    public String removeLanguage(List<String> language){return repository.removeLanguage(language);}

    public String updateLanguageLevel(Language language){return repository.updateLanguageLevel(language);}

    public String addMultipleLanguages(String sid,List<Language> languages){return repository.addMultipleLanguages(sid, languages);}

    public String addNewLanguage(String sid,Language language){return repository.addNewLanguage(sid,language);}

    public List<Student> byLanguageLevel(Language language){return repository.byLanguageLevel(language);}

    public List<Student> byLanguages(List<String> language){return repository.byLanguages(language);}

    public List<Student> byAgeNYears(Integer age,List<Integer> years){
        return repository.byAgeNYears(age, years);
    }

    public List<Student> byYears(List<Integer> years){
       return repository.byYears(years);
    }

    public Student getStudent(String id){
        return repository.getSingle(id);
    }

    public List<Student> findAll(){
        return repository.getAll();
    }

    public List<Student> getByNameNAges(List<String> name,List<Integer> age){
        return repository.getByNameNAges(name,age);
    }


    public String editStudent(Student stud){
        return repository.updateStudent(stud);
    }

    public String deleteStudent(String sid){
        return repository.deleteStudent(sid);
    }

    public String deleteByGreaterThanAge(Integer age){
        return repository.deleteByGreaterThanAge(age);
    }
}
