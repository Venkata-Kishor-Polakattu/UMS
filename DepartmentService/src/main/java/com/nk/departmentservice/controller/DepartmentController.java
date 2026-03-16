package com.nk.departmentservice.controller;


import com.nk.departmentservice.beans.Department;
import com.nk.departmentservice.service.DepartmentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentServiceImpl service;

    @GetMapping("/byclgid/{cid}")
    public List<Department> getAllDeptByClgId(@PathVariable String cid){return service.getAllDeptByClgId(cid);}

    @GetMapping
    public List<Department> getAll(){return service.getAll();}

    @GetMapping("/{id}")
    public Department getDept(@PathVariable String id){
        return service.getDept(id);
    }

    @PostMapping("/new")
    public String createDept(@RequestBody Department department){
        return service.createDept(department);
    }

    @PatchMapping("/edit")
    public String editDept(@RequestBody Department dept){
        return service.editDept(dept);
    }

    @PatchMapping("/editName/{did}")
    public String updateName(@PathVariable String did,@RequestParam String name){
      return service.updateName(did,name);
    }

    @DeleteMapping("/{did}")
    public String deleteDept(@PathVariable String did){
        return service.deleteDept(did);
    }
}