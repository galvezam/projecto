package com.galvez.projecto.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDto {
    
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;
    
    private CompanyDto companyDto;
    private Long companyDtoId;
//    private ProjectDto projectDto;
    private List<EmployeeDto> employees;
    private List<Long> employeesId;

    private List<ProjectDto> projects;
    private List<Long> projectsId;


    private String totalRevenue;

    private String totalRevenueAfterTax;

}
