package com.test.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.project.dto.StudentDTO;
import com.test.project.entity.Student;
import com.test.project.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class StudentControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; // from Jackson

    @Autowired
    private StudentRepository studentRepository;

    @Container
    private final static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            DockerImageName.parse("postgis/postgis:16-3.4-alpine").asCompatibleSubstituteFor("postgres")
    ).withInitScript("init.sql");

    @DynamicPropertySource
    static void configurePostgres(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setup() {
        studentRepository.deleteAll();
    }

    @Test
    void testCreateStudent() throws Exception {
        StudentDTO lakshitha = getStudentDTO();

        mockMvc.perform(
                post("/api/v1/student/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lakshitha))
        ).andExpect(
                result -> {
                    if (result.getResponse().getStatus() != 200) {
                        throw new AssertionError("Expected status 200 but got " + result.getResponse().getStatus());
                    }
                }
        );
    }

    @Test
    void testFetchAllStudents() throws Exception {
        studentRepository.save(getStudent());

        ResultActions response = mockMvc.perform(
                get("/api/v1/student/fetchAll")
                        .accept(MediaType.APPLICATION_JSON)
        );

        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(1));
        response.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Lakshitha"));
        response.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Srimal"));
    }

    private StudentDTO getStudentDTO() {
        return StudentDTO.builder().firstName("Lakshitha").lastName("Srimal").email("rmlc.srimal@gmail.com").build();
    }

    private Student getStudent() {
        return Student.builder().firstName("Lakshitha").lastName("Srimal").email("rmlc.srimal@gmail.com").build();
    }

}