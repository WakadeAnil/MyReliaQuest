package com.example.rqchallenge.employees.model;

import lombok.Data;

import java.util.List;

@Data
public class ApiResponseEmployee {
    private String status;
    private Employee data;
    private String message;
}