package com.galvez.projecto.service.serviceImplementation;

import java.time.LocalDate;
import java.util.List;

import com.galvez.projecto.repository.CompanyRepository;
import com.galvez.projecto.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.galvez.projecto.dto.CompanyDto;
import com.galvez.projecto.dto.EmployeeDto;
import com.galvez.projecto.dto.LoginRequest;
import com.galvez.projecto.dto.ManagerDto;
import com.galvez.projecto.dto.ProjectDto;
import com.galvez.projecto.dto.Response;
import com.galvez.projecto.exception.OurException;
import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Employee;
import com.galvez.projecto.model.Manager;
import com.galvez.projecto.model.Project;
import com.galvez.projecto.utils.JWTUtils;
import com.galvez.projecto.utils.Utils;


import com.galvez.projecto.repository.ManagerRepository;
import com.galvez.projecto.repository.ProjectRepository;
import com.galvez.projecto.service.serviceInterface.CompanyServInterface;

@Service
public class CompanyServImpl implements CompanyServInterface{
    

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private ProjectRepository projectRepository;



    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            System.out.println(loginRequest.getEmail());
            System.out.println(loginRequest.getPassword());
            System.out.println(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            System.out.println("loginRequest " + loginRequest);
            Company company = companyRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User Not Found"));
            UserDetails userDetails = jwtUtils.createCompanyUserDetails(company.getEmail());
            System.out.println(userDetails);
            var token = jwtUtils.generateToken(userDetails);
            System.out.println(token);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(company.getRole());
            response.setExpirationTime("7 days");
            response.setMessage("Successful");
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Invalid Username/Password");
            // response.setMessage("Error occurred during user login " + e.getMessage());
        }
        return response;
    }



    @Override
    public Response addCompanyToDatabase(String email, String name, String password, String phoneNumber,  LocalDate dateStarted, String totalRevenue, String totalProjectCosts, String totalProfit, String totalProfitAfterTax) {
        Response response = new Response();

        try {
            Company company = null;
            if (companyRepository.existsByEmail(email)) {
                throw new OurException(email + " already exists.");
            }
            else {
                company = new Company();
                company.setEmail(email);
            }
            if (password == null) {
                // user.setPassword(passwordEncoder.encode("abc"));
                System.out.println("password is null");
            }
            else {
                company.setPassword(passwordEncoder.encode(password));
            }
            company.setName(name);
            company.setPhoneNumber(phoneNumber);
            company.setDateStarted(dateStarted);
            company.setTotalRevenue(totalRevenue);
            company.setTotalProjectCosts(totalProjectCosts);
            company.setTotalProfit(totalProfit);
            company.setTotalProfitAfterTax(totalProfitAfterTax);
            // long totalRevenueLong = Long.parseLong(totalRevenue);
            // long totalProjectCostsLong = Long.parseLong(totalProjectCosts);
            // long totalProfitLong = Long.parseLong(totalProfit);
            // long totalProfitAfterTaxLong = Long.parseLong(totalProfitAfterTax);
            companyRepository.saveCompanyToDatabase(email, name, company.getPassword(), phoneNumber, dateStarted, totalRevenue, totalProjectCosts, totalProfit, totalProfitAfterTax);
            response.setStatusCode(200);
            response.setMessage("Successful");
            CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
            response.setCompany(companyDto);
            // response.setCompanyList(companyDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving company to database " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getAllCompanies() {
        Response response = new Response();

        try {
            List<Company> companyList = companyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<CompanyDto> companyDtoList = Utils.mapCompanyListEntityToCompanyDtoList(companyList);
            companyDtoList.forEach(this::setCompanyValues);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setCompanyList(companyDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all companies " + e.getMessage());
        }

        return response;
    }

//    public Response getCompanyByManager(String managerId) {
//        return managerService.getCompanyByManager(managerId);
//    }
//
//    public Response getCompanyByEmployee(String employeeId) {
//        return employeeService.getCompanyByEmployee(employeeId);
//    }

    @Override
    public Response getManagersByCompany(String companyId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(companyId);
            // String companyName;
            List<Manager> managerList = companyRepository.findManagersByCompany(id);
            List<ManagerDto> managerDtoList = Utils.mapManagerListEntityToManagerDtoList(managerList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setManagerList(managerDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all managers from company " + e.getMessage());
        }

        return response;   
    }

    @Override
    public Response getEmployeesByCompany(String companyId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(companyId);
            // String companyName;
            List<Employee> employeeList = companyRepository.findEmployeesByCompany(id);
            List<EmployeeDto> employeeDtoList = Utils.mapEmployeeListEntityToEmployeeDtoList(employeeList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployeeList(employeeDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all employees from company " + e.getMessage());
        }

        return response;   
    }

    @Override
    public Response deleteCompany(String companyId) {
                Response response = new Response();

        try {
            Long id = Long.valueOf(companyId);
            companyRepository.findById(id).orElseThrow(() -> new OurException("Company Not Found"));
            companyRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("Successful");
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting company " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getCompanyById(String companyId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(companyId);
            Company company = companyRepository.findById(id).orElseThrow(() -> new OurException("Company Not Found"));
            CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setCompany(companyDto);
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving company " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        
        Response response = new Response();

        try {
            Company company  = companyRepository.findByEmail(email).orElseThrow(() -> new OurException("Company Not Found"));
            CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setCompany(companyDto);
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting company information." + e.getMessage());
        }
        return response;

    }

    @Override
    public Response updateCompanyProfile(Company company) {
        Response response = new Response();

        try {
            // Long id = employee.getId();
            
            Company existingCompany = companyRepository.findById(company.getId())
                    .orElseThrow(() -> new OurException("Company Not Found"));

            existingCompany.setName(company.getName());
            existingCompany.setEmail(company.getEmail());
            // existingEmployee.setRole(user.getRole());
            // Add other fields that need to be updated

            Company updatedCompany = companyRepository.save(existingCompany);
            CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(updatedCompany);
            response.setStatusCode(200);
            response.setMessage("Company profile updated successfully.");
            response.setCompany(companyDto);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating company profile " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getCompanyByEmail(String email) {
        Response response = new Response();

        try {
            Company company = companyRepository.findByEmail(email).orElseThrow(() -> new OurException("Company Not Found"));
            CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setCompany(companyDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting company by email" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getCompanyByName(String name) {
        Response response = new Response();

        try {
            Company company = companyRepository.findByName(name).orElseThrow(() -> new OurException("Company Not Found"));
            CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setCompany(companyDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting company by name" + e.getMessage());
        }
        return response;
    }

    private void setCompanyValues(CompanyDto companyDto) {
        List<Long> employeeIdList = companyDto.getEmployeesId();
        List<Employee> employeeList = employeeRepository.findAllById(employeeIdList);
        List<EmployeeDto> employeeDtoList = Utils.mapEmployeeListEntityToEmployeeDtoList(employeeList);
        companyDto.setEmployees(employeeDtoList);

        List<Long> projectIdList = companyDto.getProjectsId();
        List<Project> projectList = projectRepository.findAllById(projectIdList);
        List<ProjectDto> projectDtoList = Utils.mapProjectListEntityToProjectDtoList(projectList);
        companyDto.setProjects(projectDtoList);

        List<Long> managerIdList = companyDto.getManagersId();
        List<Manager> managerList = null;
        List<ManagerDto> managerDtoList = null;
        if (managerIdList != null) {
            managerList = managerRepository.findAllById(managerIdList);
            managerDtoList = Utils.mapManagerListEntityToManagerDtoList(managerList);
        }
        
        companyDto.setManagers(managerDtoList);

    }


    public boolean isValidCompany(String email, String password) {
        Company company = companyRepository.findByEmail(email).orElse(null);
        if (company == null) return false;
        return true;
    }



}
