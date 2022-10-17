package com.example.rqchallenge.common;

import org.springframework.stereotype.Service;

@Service
public class Constants {

    public final static String URL = "https://dummy.restapiexample.com/";
    public final static String API_VERSION = "api/v1/";
    public final static String SLASH = "/";
    public final static String EMPLOYEES ="/employees";
    public final static String EMPLOYEE = "/employee";
    public final static String CREATE_EMPLOYEE = "create";
    public final static String UPDATE_EMPLOYEE = "update/{id}";
    public final static String DELETE_EMPLOYEE = "delete";

    public final static String SEARCH_EMPLOYEE = "/search/{searchString}";
    public final static String EMPLOYEE_BY_ID = "/employee/{id}";
    public final static String HIGHEST_SALARY = "/highestSalary";
    public final static String TOP_TEN_HIGHEST_EARNING_EMPLOYEE_NAMES = "/topTenHighestEarningEmployeeNames";
}
