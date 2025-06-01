package com.test.project.service;

import com.test.project.dto.StudentDTO;
import com.test.project.entity.Student;
import com.test.project.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public boolean createStudent(StudentDTO studentDTO) {
        Student student = new Student();
        student.setFirstName(studentDTO.getFirstName());
        student.setLastName(studentDTO.getLastName());
        student.setEmail(studentDTO.getEmail());
        try {
            studentRepository.save(student);
            return true;
        } catch (Exception e) {
            // Log the exception (not shown here for brevity)
            return false;
        }
    }

    public List<StudentDTO> fetchAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream().map(student -> {
            StudentDTO dto = new StudentDTO();
            dto.setFirstName(student.getFirstName());
            dto.setLastName(student.getLastName());
            dto.setEmail(student.getEmail());
            return dto;
        }).toList();
    }

}
