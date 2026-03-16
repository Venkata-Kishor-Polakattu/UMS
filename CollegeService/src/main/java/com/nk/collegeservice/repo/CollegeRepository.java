package com.nk.collegeservice.repo;


import com.nk.commoncontracts.events.StudentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;
import com.nk.collegeservice.beans.College;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CollegeRepository{
    private final MongoTemplate mongoTemplate;

    public College checkCollege(String cid){
        College college=null;
        try {
          college=  mongoTemplate.findById(cid,College.class);
          return college;
        }catch (Exception e){
            return college;
        }
    }

    public String createCollege(College college){
        college.setDepartmentsCount(0);college.setStudentsCount(0);
        mongoTemplate.insert(college,"colleges");
       return "College created successfully";
    }

    public College findCollege(String id){
        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(id));
       return mongoTemplate.findOne(query,College.class);
    }

    public String updateCollege(College college){
        College foundCollege=mongoTemplate.findById(college.getId(),College.class,"colleges");
        if (foundCollege==null) return "No College with id :"+college.getId()+" exists";

        if (college.getDepartmentsCount()!=null &&!foundCollege.getDepartmentsCount().equals(college.getDepartmentsCount()))
            return "You can not increase or decrease existing departments count";
        if (college.getStudentsCount()!=null &&!foundCollege.getStudentsCount().equals(college.getStudentsCount()))
            return "You can not increase or decrease existing students count";
        if (college.getYearOfEstablishment()!=null &&!foundCollege.getYearOfEstablishment().equals(college.getYearOfEstablishment()))
            return "You can not modify Year of Establishment";

        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(college.getId()));

        Update update=new Update();
        update.set("name",college.getName());
        update.set("location",college.getLocation());
        //update.set("YOE",college.getYOE());
        mongoTemplate.updateFirst(query,update,"colleges");
        return "success";
    }

    public String updateClgName(String cid,String name) {
        College clg=mongoTemplate.findById(cid,College.class);
        if (clg==null) return "No College with id :"+cid+" exists";

        Query query=new Query();
        query.addCriteria(Criteria.where("_id").is(cid));

        Update update=new Update();
        update.set("name",name);

        mongoTemplate.updateFirst(query,update,College.class);
        clg.setName(name);

        return "success";
    }

    public String updateDeptCount(College college){
        Query query=new Query().addCriteria(Criteria.where("_id").is(college.getId()));

        Update update=new Update();
        update.set("departmentsCount",college.getDepartmentsCount());
        mongoTemplate.updateFirst(query,update,College.class);
        return "success";
    }

    public void updateStudentsCount(StudentCreatedEvent event){
        Query query=new Query().addCriteria(Criteria.where("_id").is(event.getCollegeId()));

        Update update=new Update();
        update.inc("studentsCount",1); //studentsCount
        mongoTemplate.updateFirst(query,update,College.class);
    }


    public String deleteCollege(String cid){

        College college=mongoTemplate.findById(cid,College.class);
        if (college==null) return "No College with "+cid+" exists";

        Query query=new Query();
        query.addCriteria(Criteria.where("collegeId").is(cid));
        mongoTemplate.remove(college);
        return "College deleted successfully";
    }

    public List<College> getByYOE(Integer YOE){
        Query query=new Query();
        query.addCriteria(Criteria.where(":yearOfEstablishment").is(YOE));
        return mongoTemplate.find(query,College.class);
    }

    public College getByName(String name){
        Query query=new Query();
        query.addCriteria(Criteria.where("name").is(name));
        return mongoTemplate.findOne(query,College.class);
    }

    public List<College> getByLocation(String loc){
        Query query=new Query();
        query.addCriteria(Criteria.where("location").is(loc));
        return mongoTemplate.find(query,College.class);
    }

    public List<College> getByLocNName(String loc,String name){
        Query query=new Query();
        query.addCriteria(Criteria.where("location").is(loc).and("name").is(name));
        return mongoTemplate.find(query,College.class);
    }

    public List<College> getByLocNYOE(String loc,Integer YOE){
        Query query=new Query();
        query.addCriteria(Criteria.where("location").is(loc).and("yearOfEstablishment").is(YOE));
        return mongoTemplate.find(query,College.class);
    }

    public List<College> getByNameNYOE(String name,Integer YOE){
        Query query=new Query();
        query.addCriteria(Criteria.where("name").is(name).and("yearOfEstablishment").is(YOE));
        return mongoTemplate.find(query,College.class);
    }

    public List<College> getByLocNNameNYOE(String loc,String name,Integer YOE){
        Query query=new Query();
        query.addCriteria(Criteria.where("location").is(loc).and("name").is(name).and("yearOfEstablishment").is(YOE));

        return mongoTemplate.find(query,College.class);
    }
}