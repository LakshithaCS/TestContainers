package com.test.project.controller;

import com.test.project.dto.StudentDTO;
import com.test.project.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create")
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        boolean isCreated = studentService.createStudent(studentDTO);
        if (isCreated) {
            return ResponseEntity.ok(studentDTO);
        } else {
            return ResponseEntity.status(500).build(); // Return 500 if creation fails
        }
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<StudentDTO>> fetchAllStudents() {
        return ResponseEntity.ok(studentService.fetchAllStudents());
    }

}
