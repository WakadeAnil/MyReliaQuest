package com.example.rqchallenge;

import com.example.rqchallenge.common.Constants;
import com.example.rqchallenge.model.CreateEmployeeRequest;
import com.example.rqchallenge.model.Employee;
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

import static org.junit.jupiter.api.Assertions.*;
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

    private CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
    private int employeeId = 0;

    //@Test
    void shouldCreateEmployee() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setAge(32);
        createEmployeeRequest.setName("test name");
        createEmployeeRequest.setSalary(50000);

        String result = mockMvc.perform(post(Constants.EMPLOYEE).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createEmployeeRequest)))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();
        Employee employee = objectMapper.readValue(result, Employee.class);
        employeeId = employee.getId();
    }

    //@Test
    void shouldGetEmployee() throws Exception {
        CreateEmployeeRequest createEmployeeRequest = new CreateEmployeeRequest();
        createEmployeeRequest.setAge(32);
        createEmployeeRequest.setName("test name");
        createEmployeeRequest.setSalary(50000);
        mockMvc.perform(get("/employee/{id}", 8489)).andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andDo(print());
    }

    //@Test
    void shouldGetAllEmployees() throws Exception {
        String result = mockMvc.perform(get(Constants.EMPLOYEES)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<String> allEmployees = objectMapper.readValue(result, List.class);
        assertNotNull(allEmployees);
    }

    //@Test
    void shouldGetTopTenEmployees() throws Exception {
        String result = mockMvc.perform(get(Constants.TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES)).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<String> topTenEmployees = objectMapper.readValue(result, List.class);
        assertEquals(10, topTenEmployees.size());
    }

    //@Test
    void shouldSearchEmployee() throws Exception {
        String result = mockMvc.perform(get("/search/{searchString}", "test name")).andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<CreateEmployeeRequest> employees = objectMapper.readValue(result, List.class);
        assertNotNull(employees);
        assertTrue(employees.stream().filter(e -> e.getName().equalsIgnoreCase("test name")) != null);
    }
}