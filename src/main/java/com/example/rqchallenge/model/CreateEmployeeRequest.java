package com.example.rqchallenge.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateEmployeeRequest {
    @JsonProperty("name")
    private String name;
    @JsonProperty("salary")
    private int salary;
    @JsonProperty("age")
    private int age;
}