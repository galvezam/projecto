package com.galvez.projecto.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDto {
    
    private Long id;
    private String projectName;
    private String projectAddress;
    private String projectType;
    private String projectDescription;

    private LocalDate projectDateStarted;
    private LocalDate projectDateFinished;

    private String resourceCost;
    private String projectRevenue;
    private String projectProfit;

    private List<EmployeeDto> employees;
    private List<Long> employeesId;

    private EmployeeDto employeeDto;
    private Long employeeDtoId;

    private ManagerDto managerDto;
    private Long managerDtoId;

    private CompanyDto companyDto;
    private Long companyDtoId;
}
