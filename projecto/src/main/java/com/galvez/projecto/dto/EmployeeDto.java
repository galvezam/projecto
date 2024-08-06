package com.galvez.projecto.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EmployeeDto {
    private Long id;
    private String email;
    private String name;
    private String phoneNumber;

    private ProjectDto currentProjectDto;
    private Long currentProjectDtoId;

    private List<ProjectDto> projects;
    private List<Long> projectsId;

    private CompanyDto companyDto;
    private Long companyDtoId;

    private ManagerDto managerDto;
    private Long managerDtoId;


    private String totalRevenue;
    private String totalRevenueAfterTax;
}
