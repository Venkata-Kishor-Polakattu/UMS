package com.nk.studentservice.repo;

import com.mongodb.client.result.UpdateResult;
import com.nk.studentservice.beans.Language;
import com.nk.studentservice.beans.Student;
import com.nk.studentservice.filters.DeptYearFilter;
import com.nk.studentservice.service.StudentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class StudentRepository {

    private final MongoTemplate mongoTemplate;

    public String createStudent(Student student){
        // check duplicates
        if (mongoTemplate.findById(student.getId(),Student.class,"students")!=null) return "Student with id :"+student.getId()+" already exist";

        mongoTemplate.save(student,"students");
        return "Student added successfully";
    }


    public String updateStudent(Student student){
        String msg="You can not modify {0}";
        //check student existence
        Student foundStudent=getSingle(student.getId());
        if (foundStudent==null) return "Student does not exist with id :"+student.getId();
        if (student.getId()!=null) return String.format(msg," college");
        if (student.getDepartmentId()!=null) return String.format(msg," Department");


        foundStudent.setName(student.getName());
        foundStudent.setAge(student.getAge());
        foundStudent.setYear(student.getYear());


        mongoTemplate.save(foundStudent,"students");
        return "Student updated successfully";
    }

    // Aggregations

    public List<Document> maxLevelOfEachLanguage(){
        Aggregation aggregation=Aggregation.newAggregation(
                Aggregation.unwind("languages"),

                Aggregation.group("languages.name")
                        .max("languages.level").as("maxLevel"),

                Aggregation.project("maxLevel")
                        .and("_id").as("language")
        );

        AggregationResults<Document> students = mongoTemplate.aggregate(aggregation, "students", Document.class);

        return students.getMappedResults();
    }

    public List<Document> multilingualStudents(){
        Aggregation aggregation=Aggregation.newAggregation(
          Aggregation.project("name","languages")
                  .and(ArrayOperators.Size.lengthOfArray("languages")).as("languagesCount"),
                Aggregation.match(Criteria.where("languagesCount").gt(1))
        );

        AggregationResults<Document> students = mongoTemplate.aggregate(aggregation, "students", Document.class);

        return students.getMappedResults();
    }

    public List<Document> topLanguage(){
        Aggregation aggregation=Aggregation.newAggregation(
                Aggregation.unwind("languages"),
                Aggregation.group("languages.name")
                        .count().as("students"),
                Aggregation.sort(Sort.Direction.DESC,"students"),
                Aggregation.limit(1),
                Aggregation.project("students")
                        .and("_id").as("language")
        );

        AggregationResults<Document> results=mongoTemplate.aggregate(aggregation,"students",Document.class);
        return results.getMappedResults();
    }

    public List<Document> avgLevelPerLanguage(){
        Aggregation aggregation=Aggregation.newAggregation(
          Aggregation.unwind("languages"),
                Aggregation.group("languages.name")
                        .avg("languages.level").as("Average")
        );

        AggregationResults<Document> results=mongoTemplate.aggregate(aggregation, "students",Document.class);
        return results.getMappedResults();
    }

    public List<Document> studentsPerLanguage(){
        Aggregation aggregation=Aggregation.newAggregation(
                Aggregation.unwind("languages"),
                Aggregation.group("languages.name")
                        .count().as("studentsCount")
        );

        AggregationResults<Document> results=mongoTemplate.aggregate(aggregation,"students",Document.class);
        return results.getMappedResults();
    }


    public String increaseLanguagesLevel(List<String> languages){
        Query query = Query.query(Criteria.where("languages.name").in(languages));

        Update update=new Update();
        update.inc("languages.$[lan].level",1)
                .filterArray(Criteria.where("lan.name").in(languages));

        long count = mongoTemplate.count(query, Student.class);

        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Student.class);

        return "Out of "+count+" "+updateResult.getModifiedCount()+" are increased";
    }

    public String removeLanguage(List<String> language){
        Query query=new Query();
        query.addCriteria(Criteria.where("languages.name").is(language));

        Update update=new Update();
        update.pull("languages",
                Criteria.where("name").in(language)
                );

        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Student.class);

        return "From "+updateResult.getModifiedCount()+" students the "+language+" is removed";
    }


    public String updateLanguageLevel(Language language){
        Query query=new Query();
        query.addCriteria(
                Criteria.where("languages.name").is(language.getName())
        );

        long count = mongoTemplate.count(query, Student.class);

        Update update=new Update();
        update.set("languages.$[lang].level",language.getLevel())
                .filterArray(Criteria.where("lang.name").is(language.getName()));

        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Student.class);

        return updateResult.getModifiedCount()+" are updated out of "+count;
    }

    public String addMultipleLanguages(String sid,List<Language> languages){
        Query query=Query.query(Criteria.where("_id").is(sid));

        Update update=new Update();
        update.push("languages").each(languages);

        mongoTemplate.updateFirst(query, update, Student.class);

        return "Successfully added "+languages.size()+" languages added to "+sid;
    }

    public String addNewLanguage(String sid,Language language){
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(sid));
        Student student=mongoTemplate.findOne(query,Student.class);

        if (student==null)return "Student does not exist";

        Update update=new Update();
        update.push("languages",language);

        mongoTemplate.updateFirst(query,update,Student.class);
        return "Language Added successfully";
    }

    public List<Student> byLanguageLevel(Language language){
        Query query=new Query();
        query.addCriteria(
                Criteria.where("languages").elemMatch(
                        Criteria.where("name").is(language.getName())
                                .and("level").gte(language.getLevel())
                )
        );

        return mongoTemplate.find(query, Student.class);
    }

    public List<Student> byLanguages(List<String> language){
        Query query=new Query();
        query.addCriteria(
                Criteria.where("languages.name").in(language)
        );
        return mongoTemplate.find(query,Student.class);
    }

    //1️⃣1️⃣ Students where:year = 2 OR year = 3 (Hint: use in())

    public List<Student> byYears(List<Integer> years){
        Query query=new Query();
        Criteria criteria=new Criteria();
        if (years!=null && !years.isEmpty()){
            criteria.and("year").in(years);
        }

        query.addCriteria(criteria);
        return mongoTemplate.find(query,Student.class);
    }

    //1️⃣2️⃣ Students where:age > 20 AND (year = 3 OR year = 4)
    public List<Student> byAgeNYears(Integer age,List<Integer> years){
        Query query=new Query();
        Criteria criteria=new Criteria();
        criteria.and("age").is(age);

        if (years!=null && !years.isEmpty()){
            criteria.and("year").in(years);
        }
        query.addCriteria(criteria);
       return mongoTemplate.find(query,Student.class);
    }

   //1️⃣3️⃣ Departments where:StudCount > 100 OR HOD = "Suresh"



   //1️⃣4️⃣ Students where:(dept_id = X AND year = 3)OR(dept_id = Y AND year = 4)(Requires nested AND inside OR)
    public List<Student> byDeptIdNYearComb(List<DeptYearFilter> deptYearFilterList){
        Query query=new Query();

        List<Criteria> orBlocks=new ArrayList<>();


        for (DeptYearFilter combo:deptYearFilterList){
            Criteria ct=Criteria.where("dept_id").is(combo.getDeptId()).
                    and("year").is(combo.getYear());

            orBlocks.add(ct);
        }

        if (!orBlocks.isEmpty()){
            query.addCriteria(
                    new Criteria().orOperator(orBlocks.toArray(new Criteria[0]))
            );
        }

        System.out.println(query);

       return mongoTemplate.find(query,Student.class,"students");
    }



    public List<Student> getAll(){
       return mongoTemplate.findAll(Student.class,"students");
    }

    public Student getSingle(String sid){
        return mongoTemplate.findById(sid,Student.class,"students");
    }



    public List<Student> getByAgeGreaterNYearGreater(Integer age,Integer year1,Integer year2){
        Query query=new Query();
        query.addCriteria(Criteria.where("age").gte(age));
        query.addCriteria(
                new Criteria().orOperator(
                        Criteria.where("year").is(year1),
                        Criteria.where("year").is(year2)
                        ));
        return mongoTemplate.find(query,Student.class,"students");
    }

    public List<Student> getByNameNAges(List<String> names,List<Integer> ages){
        Query query=new Query();
        Criteria criteria=new Criteria();
        if(names!=null && !names.isEmpty()){
            criteria.and("name").in(names);
        }
        if(ages!=null && !ages.isEmpty()){
            criteria.and("age").in(ages);
        }

        query.addCriteria(criteria);

        return mongoTemplate.find(query,Student.class,"students");
    }



    public String updateClgReference( String clgId,String clgName){
        Query query=new Query();
        Criteria criteria=Criteria.where("c_id").is(clgId);

        Update update=new Update();
        update.set("c_name",clgName);

        query.addCriteria(criteria);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Student.class);

        return updateResult.getModifiedCount()+" Students are updated";
    }

    public String updateDeptReference( String did,String name){
        Query query=new Query();
        Criteria criteria=Criteria.where("dept_id").is(did);


        Update update=new Update();
        update.set("dept_name",name);

        query.addCriteria(criteria);
        UpdateResult updateResult = mongoTemplate.updateMulti(query, update, Student.class);

        return updateResult.getModifiedCount()+" Students are updated";
    }

    public String deleteStudent(String sid){
        Student foundStudent=getSingle(sid);
        if (foundStudent==null) return "Student does not exist with id :"+sid;

        //decrease stud count in college
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(foundStudent.getCollegeId()));

        Update update=new Update().inc("StudCount",-1);
        mongoTemplate.updateFirst(query,update,"colleges");


        //decrease stud count in dept
        Query query1=new Query();
        query1.addCriteria(Criteria.where("_id").is(foundStudent.getId()));

        Update update2=new Update().inc("StudCount",-1);
        mongoTemplate.updateFirst(query1,update2,"departments");


        mongoTemplate.remove(foundStudent,"students");

        return "Student deleted successfully";
    }

    public String deleteByGreaterThanAge(Integer age){
        Query query=new Query();
        query.addCriteria(Criteria.where("age").gt(age));

        List<Student> students = mongoTemplate.find(query, Student.class);

        Map<String, Long> clgCounts=students.stream().collect(
                Collectors.groupingBy(
                        Student::getCollegeId,Collectors.counting()
                ));

        Map<String, Long> deptCounts=students.stream().collect(
                Collectors.groupingBy(
                        Student::getDepartmentId,Collectors.counting()
                )
        );

        mongoTemplate.remove(query,Student.class);

        for (String clgId:clgCounts.keySet()){
            Query clgQuery=new Query();
            clgQuery.addCriteria(Criteria.where("_id").is(clgId));
            Update update=new Update();
            update.inc("StudCount",-clgCounts.get(clgId));

            //mongoTemplate.updateFirst(clgQuery,update,College.class);
        }

        for (String deptId:deptCounts.keySet()){
            Query deptQuery=new Query();
            deptQuery.addCriteria(Criteria.where("_id").is(deptId));
            Update update=new Update();
            update.inc("StudCount",-deptCounts.get(deptId));

            //mongoTemplate.updateFirst(deptQuery,update,Department.class);
        }

        return "Students deleted successfully";
    }
}
