package com.example.rqchallenge.model;

import lombok.Data;

@Data
public class ApiResponseEmployee {
    private String status;
    private Employee data;
    private String message;
}