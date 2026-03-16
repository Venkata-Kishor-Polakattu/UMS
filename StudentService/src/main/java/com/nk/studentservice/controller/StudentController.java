package com.nk.studentservice.controller;

import com.nk.studentservice.beans.Language;
import com.nk.studentservice.beans.Student;
import com.nk.studentservice.filters.DeptYearFilter;
import com.nk.studentservice.repo.StudentSearchRepository;
import com.nk.studentservice.searchDocuments.StudentSearchDocument;
import com.nk.studentservice.service.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentServiceImpl service;
    private final StudentSearchRepository searchRepository;

    @GetMapping("/students/{name}")
    public List<StudentSearchDocument> searchStudents(@PathVariable String name){
        return searchRepository.findByName(name);
    }

    @GetMapping("/college/{college}")
    public List<StudentSearchDocument> searchByCollege(@PathVariable String college){
        return searchRepository.findByCollegeName(college);
    }

    // Aggregations

    @GetMapping("/languages/maxLevels")
    public List<Document> maxLevelOfEachLanguage(){return service.maxLevelOfEachLanguage();}

    @GetMapping("/multilinguals")
    public List<Document> multilingualStudents(){return service.multilingualStudents();}

    @GetMapping("/languages/top")
    public List<Document> topLanguage(){return service.topLanguage();}

    @GetMapping("/avgPerLanguage")
    public List<Document> avgLevelPerLanguage(){return service.avgLevelPerLanguage();}

    @GetMapping("/perLanguage")
    public List<Document> studentsPerLanguage(){return service.studentsPerLanguage();}

    @PatchMapping("/languages/increase")
    public String increaseLanguagesLevel(@RequestParam List<String> languages){return service.increaseLanguagesLevel(languages);}

    @PatchMapping("/languages/remove")
    public String removeLanguage(@RequestParam List<String> language){return service.removeLanguage(language);}

    @PatchMapping("/languages/level/update")
    public String updateLanguageLevel(@RequestBody Language language){return service.updateLanguageLevel(language);}

    @PatchMapping("/languages/new/multiple/{sid}")
    public String addMultipleLanguages(@PathVariable String sid,@RequestBody List<Language> languages){return service.addMultipleLanguages(sid, languages);}

    @PatchMapping("/languages/new/{sid}")
    public String addNewLanguage(@PathVariable String sid,@RequestBody Language language){return service.addNewLanguage(sid,language);}

    @GetMapping("/languages/level")
    public List<Student> byLanguageLevel(@RequestBody Language language){return service.byLanguageLevel(language);}

    @GetMapping("/languages")
    public List<Student> byLanguages(@RequestParam List<String> language){return service.byLanguages(language);}

    @GetMapping("/byDeptIdNYearComb")
    public List<Student> byDeptIdNYearComb(@RequestBody List<DeptYearFilter> deptYearFilterList){
        return service.byDeptIdNYearComb(deptYearFilterList);
    }

    @GetMapping("/byAgeNYears")
    public List<Student> byAgeNYears(@RequestParam Integer age,@RequestParam List<Integer> year){
        return service.byAgeNYears(age, year);
    }

    @GetMapping("/byYears")
    public List<Student> byYears(@RequestParam List<Integer> years){
        return service.byYears(years);
    }

    @GetMapping
    public List<Student> findAll(){
       return service.findAll();
    }

    @GetMapping("/{sid}")
    public Student getStudent(@PathVariable String sid){
       return service.getStudent(sid);
    }

    @GetMapping("/byNamesAndAges")
    public List<Student> getByNameNAges(@RequestParam List<String> name,@RequestParam List<Integer> age){
       return service.getByNameNAges(name,age);
    }

    @PostMapping("/new")
    public String createStudent(@RequestBody Student std){
        return service.createStudent(std);
    }

    @PatchMapping("/edit")
    public String updateStd(@RequestBody Student std){
        return service.editStudent(std);
    }

    @DeleteMapping("/delete/{sid}")
    public String deleteStd(@PathVariable String sid){
        return service.deleteStudent(sid);
    }

    @DeleteMapping("/byGreaterThanAge/{age}")
    public String deleteByGreaterThanAge(@PathVariable Integer age){
        return service.deleteByGreaterThanAge(age);
    }
}
