package com.galvez.projecto.utils;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.galvez.projecto.service.serviceInterface.EmployeeServInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.galvez.projecto.dto.CompanyDto;
import com.galvez.projecto.dto.EmployeeDto;
import com.galvez.projecto.dto.ManagerDto;
import com.galvez.projecto.dto.ProjectDto;
import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Employee;
import com.galvez.projecto.model.Manager;
import com.galvez.projecto.model.Project;
import com.galvez.projecto.repository.CompanyRepository;
import com.galvez.projecto.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

// @Service
public class Utils {
    
    private static final String ALPHANUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom secureRandom = new SecureRandom();


//    @Autowired
//    private EmployeeRepository employeeRepository;

    // @Autowired
    private static EmployeeServInterface employeeService;

    public static String generateRandomConfirmationCode(int length) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(ALPHANUMERIC_STRING.length());
            char randomChar = ALPHANUMERIC_STRING.charAt(randomIndex);
            str.append(randomChar);
        }
        return str.toString();
    }


    public static EmployeeDto mapEmployeeEntityToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = new EmployeeDto();

        employeeDto.setId(employee.getId());
        employeeDto.setName(employee.getName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setPhoneNumber(employee.getPhoneNumber());

        // map employee.getProject() from project entity to project dto?, same for other dtos
//        List<ProjectDto> projectDtoList = null;
        List<Long> projectDtoIdList = null;
        if (employee.getProjectList() != null) {
//            projectDtoList= mapProjectListEntityToProjectDtoList(employee.getProjectList());
            projectDtoIdList = mapProjectIdListEntityToProjectDtoIdList(employee.getProjectList());
        }
//        ProjectDto projectDto = null;
        ProjectDto currentProjectDto = null;
        if (employee.getCurrentProject() != null) {
//            projectDto = mapProjectEntityToProjectDto(employee.getCurrentProject());
//            currentProjectDto = mapProjectEntityToProjectDto(employee.getCurrentProject());
        }
        ManagerDto managerDto = null;
        if (employee.getManager() != null) {
//            managerDto = mapManagerEntityToManagerDto(employee.getManager());
        }
//        System.out.println(employee.getCompany());
//        System.out.println(employee.getCompany().getId());
//        Optional<Company> company = employeeRepository.findCompanyByEmployee(employee.getId());
//        CompanyDto companyDto = employeeService.getCompanyByEmployee("" + employee.getId()).getCompany();
        // CompanyDto companyDto = mapCompanyEntityToCompanyDto(employee.getCompany());

        // employeeDto.setProjects(projectDtoList);

        employeeDto.setProjectsId(projectDtoIdList);

        // employeeDto.setCurrentProjectDto(projectDto);
        Long currentProjectId = null;
        if (employee.getCurrentProject() != null) {
            currentProjectId = employee.getCurrentProject().getId();
        }
        employeeDto.setCurrentProjectDtoId(currentProjectId);

        employeeDto.setCompanyDtoId(employee.getCompany().getId());

        // employeeDto.setManagerDto(managerDto);
        Long managerId = null;
        if (employee.getManager() != null) {
            managerId = employee.getManager().getId();
        }
        employeeDto.setManagerDtoId(managerId);

        employeeDto.setTotalRevenue(employee.getTotalRevenue());
        employeeDto.setTotalRevenueAfterTax(employee.getTotalRevenueAfterTax());
        // employeeDto.setRole(employee.getRole());
        return employeeDto;
    }

    public static ManagerDto mapManagerEntityToManagerDto(Manager manager) {
        ManagerDto managerDto = new ManagerDto();

        if (manager == null) return managerDto;

        managerDto.setId(manager.getId());
        managerDto.setName(manager.getName());
        managerDto.setEmail(manager.getEmail());
        managerDto.setPhoneNumber(manager.getPhoneNumber());

        // List<EmployeeDto> employeeDtoList = mapEmployeeListEntityToEmployeeDtoList(manager.getEmployees());
        // managerDto.setEmployees(employeeDtoList);

        List<Long> employeeDtoIdList = mapEmployeeIdListEntityToEmployeeDtoIdList(manager.getEmployees());
        managerDto.setEmployeesId(employeeDtoIdList);
        

        // List<ProjectDto> projectDtoList = mapProjectListEntityToProjectDtoList(manager.getProjects());
        // managerDto.setProjects(projectDtoList);

        List<Long> projectDtoIdList = mapProjectIdListEntityToProjectDtoIdList(manager.getProjects());
        managerDto.setProjectsId(projectDtoIdList);

        // CompanyDto companyDto = mapCompanyEntityToCompanyDto(manager.getCompany());
        // managerDto.setCompanyDto(companyDto);
        managerDto.setCompanyDtoId(manager.getCompany().getId());
        

        managerDto.setTotalRevenue(manager.getTotalRevenue());
        managerDto.setTotalRevenueAfterTax(manager.getTotalRevenueAfterTax());
        
        return managerDto;
    }

    public static CompanyDto mapCompanyEntityToCompanyDto(Company company) {
        CompanyDto companyDto = new CompanyDto();

        if (company == null) return companyDto;

        companyDto.setId(company.getId());
        companyDto.setName(company.getName());
        companyDto.setEmail(company.getEmail());
        companyDto.setPhoneNumber(company.getPhoneNumber());

        // List<EmployeeDto> employeeDtoList = mapEmployeeListEntityToEmployeeDtoList(company.getEmployees());
        List<Long> employeeDtoIdList = mapEmployeeIdListEntityToEmployeeDtoIdList(company.getEmployees());

        // List<ManagerDto> managerDtoList = mapManagerListEntityToManagerDtoList(company.getManagers());
        List<Long> managerDtoIdList = mapManagerIdListEntityToManagerDtoIdList(company.getManagers());

//        List<ProjectDto> projectDtoList = null;
        List<Long> projectDtoIdList = null;
        if (company.getProjects() != null) {
//            projectDtoList = mapProjectListEntityToProjectDtoList(company.getProjects());
            projectDtoIdList = mapProjectIdListEntityToProjectDtoIdList(company.getProjects());
        }
        

        // companyDto.setEmployees(employeeDtoList);
        companyDto.setEmployeesId(employeeDtoIdList);

        // companyDto.setManagers(managerDtoList);
        companyDto.setManagersId(managerDtoIdList);

        // companyDto.setProjects(projectDtoList);
        companyDto.setProjectsId(projectDtoIdList);

        companyDto.setTotalRevenue(company.getTotalRevenue());
        companyDto.setTotalProjectCosts(company.getTotalProjectCosts());
        companyDto.setTotalProfit(company.getTotalProfit());
        companyDto.setTotalProfitAfterTax(company.getTotalProfitAfterTax());

        return companyDto;
    }

    public static ProjectDto mapProjectEntityToProjectDto(Project project) {
        ProjectDto projectDto = new ProjectDto();

        projectDto.setId(project.getId());

        projectDto.setProjectName(project.getProjectName());
        projectDto.setProjectAddress(project.getProjectAddress());
        projectDto.setProjectType(project.getProjectType());
        projectDto.setProjectDescription(project.getProjectDescription());

        projectDto.setProjectDateStarted(project.getProjectDateStarted());
        projectDto.setProjectDateFinished(project.getProjectDateFinished());

        projectDto.setProjectRevenue(project.getProjectRevenue());
        projectDto.setResourceCost(project.getResourceCost());
        projectDto.setProjectProfit(project.getProjectProfit());

//        List<EmployeeDto> employeeDtoList = mapEmployeeListEntityToEmployeeDtoList(project.getEmployeeList());
        List<Long> employeeDtoIdList = mapEmployeeIdListEntityToEmployeeDtoIdList(project.getEmployeeList());
        
        EmployeeDto employeeDto = mapEmployeeEntityToEmployeeDto(project.getEmployee());

//        projectDto.setEmployees(employeeDtoList);
        projectDto.setEmployeesId(employeeDtoIdList);

        Long employeeId = null;
        if (project.getEmployee() != null) {
            employeeId = project.getEmployee().getId();
        }
        projectDto.setEmployeeDtoId(employeeId);
        // projectDto.setEmployeeDtoId(project.getEmployee().getId());
        Long managerId = null;
        if (project.getManager() != null) {
            managerId = project.getManager().getId();
        }
//        ManagerDto managerDto = mapManagerEntityToManagerDto(project.getManager());
//        projectDto.setManagerDto(managerDto);
         projectDto.setManagerDtoId(managerId);

//        CompanyDto companyDto = mapCompanyEntityToCompanyDto(project.getCompany());
//        projectDto.setCompanyDto(companyDto);
         projectDto.setCompanyDtoId(project.getCompany().getId());

        return projectDto;
    }


    public static List<EmployeeDto> mapEmployeeListEntityToEmployeeDtoList(List<Employee> employeeList) {
        return employeeList.stream().map(Utils:: mapEmployeeEntityToEmployeeDto).collect(Collectors.toList());
    }

    public static List<Long> mapEmployeeIdListEntityToEmployeeDtoIdList(List<Employee> employeeList) {
        return employeeList.stream().map(Employee::getId).collect(Collectors.toList());
        // return employeeList.stream().map(Utils:: mapEmployeeEntityToEmployeeDto).collect(Collectors.toList());
    }

    public static List<ManagerDto> mapManagerListEntityToManagerDtoList(List<Manager> managerList) {
        return managerList.stream().map(Utils:: mapManagerEntityToManagerDto).collect(Collectors.toList());
    }

    public static List<Long> mapManagerIdListEntityToManagerDtoIdList(List<Manager> managerList) {
        return managerList.stream().map(Manager::getId).collect(Collectors.toList());
    }


    public static List<CompanyDto> mapCompanyListEntityToCompanyDtoList(List<Company> companyList) {
        return companyList.stream().map(Utils:: mapCompanyEntityToCompanyDto).collect(Collectors.toList());
    }

    public static List<Long> mapCompanyIdListEntityToCompanyDtoIdList(List<Company> companyList) {
        return companyList.stream().map(Company::getId).collect(Collectors.toList());
    }

    public static List<ProjectDto> mapProjectListEntityToProjectDtoList(List<Project> projectList) {
        return projectList.stream().map(Utils:: mapProjectEntityToProjectDto).collect(Collectors.toList());
    }

    public static List<Long> mapProjectIdListEntityToProjectDtoIdList(List<Project> projectList) {
        return projectList.stream().map(Project::getId).collect(Collectors.toList());
    }

}
