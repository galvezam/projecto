package com.galvez.projecto.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;

    private List<EmployeeDto> employees;
    private List<Long> employeesId;

    private List<ManagerDto> managers;
    private List<Long> managersId;

    private List<ProjectDto> projects;
    private List<Long> projectsId;

    private String totalRevenue;

    private String totalProjectCosts;

    private String totalProfit;

    private String totalProfitAfterTax;
}
