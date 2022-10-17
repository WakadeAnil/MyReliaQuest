package com.example.rqchallenge.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponseEmployeeList {
    private String status;
    private List<Employee> data;
    private String message;
}