package com.galvez.projecto.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response {

    private int statusCode;
    private String message;
    private String token;

    // private String role;

    private String expirationTime;
    // private String bookingConfirmationCode;

    private EmployeeDto employee;
    private ManagerDto manager;
    private CompanyDto company;
    private List<EmployeeDto> employeeList;
    private List<ManagerDto> managerList;
    private List<CompanyDto> companyList;

    private ProjectDto project;
    private List<ProjectDto> projectList;

    private String role;
    
}
