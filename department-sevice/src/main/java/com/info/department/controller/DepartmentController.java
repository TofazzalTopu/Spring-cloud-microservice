package com.info.department.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.info.department.model.Department;
import com.info.department.service.DepartmentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @Value("${server.port}")
    String port;
    public int count = 0;


    @PostMapping
    public ResponseEntity<?> save(@RequestBody Department department) throws URISyntaxException {
        return ResponseEntity.created(new URI("/")).body(departmentService.save(department));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Department department = departmentService.findById(id);
        if (Objects.isNull(department)) department = new Department(id, "Math");
        System.out.println("counter: " + ++count);
        System.out.println("Round robin port: " + port);
        log.info("department: " + department);
        return ResponseEntity.ok().body(department);
    }

}
