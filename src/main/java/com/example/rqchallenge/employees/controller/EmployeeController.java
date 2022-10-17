package com.example.rqchallenge.employees.controller;

import com.example.rqchallenge.employees.common.Constants;
import com.example.rqchallenge.employees.model.ApiResponseEmployee;
import com.example.rqchallenge.employees.model.ApiResponseEmployeeList;
import com.example.rqchallenge.employees.model.CreateEmployeeRequest;
import com.example.rqchallenge.employees.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rqchallenge.employees.IEmployeeController;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RestController
public class EmployeeController implements IEmployeeController {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping(Constants.EMPLOYEES)
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        try{
            ResponseEntity<ApiResponseEmployeeList> response
                    = restTemplate.getForEntity(Constants.URL+Constants.API_VERSION+Constants.EMPLOYEES, ApiResponseEmployeeList.class);
            List<Employee> employees =  response.getBody().getData();
            return ResponseEntity.ok(employees);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(Constants.SEARCH_EMPLOYEE)
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString) {
        try{
            ResponseEntity<ApiResponseEmployeeList> response
                    = restTemplate.getForEntity(Constants.URL+Constants.API_VERSION+Constants.EMPLOYEES, ApiResponseEmployeeList.class);
            List<Employee> employees = response.getBody().getData().stream()
                    .filter(e -> e.getEmployeeName().equals(searchString))
                    .toList();
            return ResponseEntity.ok(employees);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(Constants.EMPLOYEE_BY_ID)
    public ResponseEntity<Employee> getEmployeeById(@PathVariable String id) {
        ResponseEntity<ApiResponseEmployee> response
                = restTemplate.getForEntity(Constants.URL+Constants.API_VERSION+Constants.EMPLOYEE+Constants.SLASH+
                id, ApiResponseEmployee.class);
        Employee employee = response.getBody().getData();
        return ResponseEntity.ok(employee);
    }

    @GetMapping(Constants.HIGHEST_SALARY)
    public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
        try{
            ResponseEntity<ApiResponseEmployeeList> response
                    = restTemplate.getForEntity(Constants.URL+Constants.API_VERSION+Constants.EMPLOYEES, ApiResponseEmployeeList.class);
            Employee employee = response.getBody().getData().stream()
                    .max((e1, e2) -> (e1.getEmployeeSalary()) - e2.getEmployeeSalary())
                    .get();
            if(employee!= null) {
                return ResponseEntity.ok(employee.getEmployeeSalary());
            } else {
                throw new Exception();
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Integer.MIN_VALUE);
        }
    }

    @GetMapping(Constants.TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES)
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        try {
            ResponseEntity<ApiResponseEmployeeList> response
                    = restTemplate.getForEntity(Constants.URL+Constants.API_VERSION+Constants.EMPLOYEES, ApiResponseEmployeeList.class);
            List<String> employeeNames = response.getBody().getData().stream()
                    .sorted((e1, e2) -> (e2.getEmployeeSalary()) - e1.getEmployeeSalary())
                    .map(e -> e.getEmployeeName() + " - " + e.getEmployeeSalary())
                    .collect(Collectors.toList());
            employeeNames.subList(10, employeeNames.size()).clear();
            if(employeeNames!= null) {
                return ResponseEntity.ok(employeeNames);
            } else {
                throw new RuntimeException();
            }
        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = Constants.EMPLOYEE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            CreateEmployeeRequest employeeRequest = mapper.convertValue(employeeInput, CreateEmployeeRequest.class);
            ResponseEntity<ApiResponseEmployee> response
                    = restTemplate.postForEntity(Constants.URL+Constants.API_VERSION+Constants.CREATE_EMPLOYEE, CreateEmployeeRequest.class, ApiResponseEmployee.class);
            System.out.println("API Response for CREATE : " + response);
            if(response!= null) {
                return ResponseEntity.ok(response.getBody().getData());
            } else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable String id) {
        try {
            System.out.println("DELETE : " + Constants.URL+Constants.API_VERSION+Constants.DELETE_EMPLOYEE+Constants.SLASH+ id);
            restTemplate.delete(Constants.URL+Constants.API_VERSION+Constants.DELETE_EMPLOYEE+Constants.SLASH+ id);
            return ResponseEntity.ok("Employee deleted successfully");
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while deleting the employee");
        }
    }
}