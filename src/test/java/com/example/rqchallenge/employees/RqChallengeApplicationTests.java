package com.example.rqchallenge.employees;

import com.example.rqchallenge.employees.common.Constants;
import com.example.rqchallenge.employees.model.CreateEmployeeRequest;
import com.example.rqchallenge.employees.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;


import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RqChallengeApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldGetTopTenEmployees() throws Exception {
        String result = mockMvc.perform(get(Constants.TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<String> topTenEmployees = objectMapper.readValue(result, List.class);
        assertEquals(10, topTenEmployees.size());
    }

    @Test
    void shouldGetAllEmployees() throws Exception {
        String result = mockMvc.perform(get(Constants.EMPLOYEES)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<String> allEmployees = objectMapper.readValue(result, List.class);
        assertNotNull(allEmployees);
    }

    @Test
    void shouldCreateEmployee() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setAge(32);
        createEmployeeRequest.setName("test name");
        createEmployeeRequest.setSalary(50000);

        mockMvc.perform(post(Constants.EMPLOYEE).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void shouldGetEmployee() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setAge(32);
        createEmployeeRequest.setName("test name");
        createEmployeeRequest.setSalary(50000);

        mockMvc.perform(get("/employee/{id}", 32)).andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(createEmployeeRequest.getName()))
                .andExpect(jsonPath("$.age").value(createEmployeeRequest.getAge()))
                .andExpect(jsonPath("$.salary").value(createEmployeeRequest.getSalary()))
                .andDo(print());
    }


}
