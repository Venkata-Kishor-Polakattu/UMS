package com.nk.collegeservice.controller;

import com.nk.collegeservice.service.CollegeService;
import com.nk.commoncontracts.respnse.CollegeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nk.collegeservice.beans.College;

@RestController
@RequestMapping("/college")
@RequiredArgsConstructor
public class CollegeController {

    private final CollegeService service;

    @PostMapping("/new")
    public ResponseEntity<String> createCollege(@RequestBody College college){
        String s=null;

        try {
           s= service.createClg(college);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(s);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @GetMapping("/check/{cid}")
    public CollegeResponseDto checkCollege(@PathVariable String cid){
        return service.checkCollege(cid);}

    @GetMapping("/{id}")
    public ResponseEntity<College> getCollege(@PathVariable String id){
        College college =null;
        try {
            college=service.getCollege(id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(college);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(college);
    }

    @PatchMapping("/edit")
    public String editCollege(@RequestBody College college){
        return service.updateClg(college);
    }

    @GetMapping("/editName/{cid}")
    public String editName(@PathVariable String cid,@RequestParam String name){
        return service.updateName(cid,name);
    }

    @PostMapping("/editDeptCount")
    public ResponseEntity<String> updateDeptCount(@RequestBody College college){
        String s = service.updateDeptCount(college);

        if (s.equals("success")){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(s);
    }

    @DeleteMapping("/{cid}")
    public String deleteCollege(@PathVariable String cid){
        return service.deleteClg(cid);
    }
}