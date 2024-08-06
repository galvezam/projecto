package com.galvez.projecto.service.serviceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import com.galvez.projecto.repository.CompanyRepository;
import com.galvez.projecto.repository.EmployeeRepository;
import com.galvez.projecto.repository.ManagerRepository;
import com.galvez.projecto.repository.ProjectRepository;
import com.galvez.projecto.service.serviceInterface.ManagerServInterface;

@Service
public class ManagerServImpl implements ManagerServInterface{
    
    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

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
            Employee employee = employeeRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User Not Found"));
            UserDetails userDetails = jwtUtils.createEmployeeUserDetails(employee.getEmail());
            System.out.println(userDetails);
            var token = jwtUtils.generateToken(userDetails);
            System.out.println(token);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(employee.getRole());
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

    // SAVES Manager TO DATABASE
    @Override
    public Response addManagerToDatabase(String email, String name, String password, String phoneNumber, String company, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax, Long companysId) {
        Response response = new Response();

        try {

            Manager manager = null;
            if (managerRepository.existsByEmail(email)) {
                throw new OurException(email + " already exists.");
            }
            else {
                manager = new Manager();
                manager.setEmail(email);
            }
            if (password == null) {
                // user.setPassword(passwordEncoder.encode("abc"));
                System.out.println("password is null");
            }
            else {
                manager.setPassword(passwordEncoder.encode(password));
            }
            manager.setName(name);
            manager.setPhoneNumber(phoneNumber);
            manager.setManagerDateStarted(dateStarted);
            Company company1 = companyRepository.findByName(company).orElse(null);
            manager.setCompany(company1);
            manager.setTotalRevenue(totalRevenue);
            manager.setTotalRevenueAfterTax(totalRevenueAfterTax);
            
            
            // long totalRevenueLong = Long.parseLong(totalRevenue);
            // long totalRevenueAfterTaxLong = Long.parseLong(totalRevenueAfterTax);
            
            managerRepository.saveManagerToDatabase(email, name, manager.getPassword(), phoneNumber, company, dateStarted, totalRevenue, totalRevenueAfterTax, companysId);
            response.setStatusCode(200);
            response.setMessage("Successful");
            ManagerDto managerDto = Utils.mapManagerEntityToManagerDto(manager);
            response.setManager(managerDto);
            // response.setCompanyList(companyDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving manager to database " + e.getMessage());
        }

        return response;
    }

    // GET ALL MANAGERS
    @Override
    public Response getAllManagers() {
        Response response = new Response();

        try {
            List<Manager> managerList = managerRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<ManagerDto> managerDtoList = Utils.mapManagerListEntityToManagerDtoList(managerList);
            managerDtoList.forEach(this::setManagerValues);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setManagerList(managerDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting all managers " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getEmployeesByManager(String managerId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(managerId);
            List<Employee> employeeList = managerRepository.findEmployeesByManager(id);
            List<EmployeeDto> employeeDtoList = Utils.mapEmployeeListEntityToEmployeeDtoList(employeeList);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployeeList(employeeDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting employees from manager " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getCompanyByManager(String managerId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(managerId);
            Company company = managerRepository.findCompanyByManager(id);
            CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setCompany(companyDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting company from manager " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteManager(String managerId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(managerId);
            managerRepository.findById(id).orElseThrow(() -> new OurException("Manager Not Found"));
            managerRepository.deleteById(id);
            response.setStatusCode(200);
            response.setMessage("Successful");
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting manager " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getManagerById(String managerId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(managerId);
            Optional<Manager> manager = managerRepository.findById(id);
            ManagerDto managerDto = manager.map(Utils::mapManagerEntityToManagerDto).orElse(null);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setManager(managerDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting company from manager " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        Response response = new Response();

        try {
            Manager manager = managerRepository.findByEmail(email).orElseThrow(() -> new OurException("Manager Not Found"));
            ManagerDto managerDto = Utils.mapManagerEntityToManagerDto(manager);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setManager(managerDto);
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting manager information " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateManagerProfile(Manager manager) {
        Response response = new Response();

        try {
            // Long id = employee.getId();
            
            Manager existingManager = managerRepository.findById(manager.getId())
                    .orElseThrow(() -> new OurException("User Not Found"));

            existingManager.setName(manager.getName());
            existingManager.setEmail(manager.getEmail());
            // existingEmployee.setRole(user.getRole());
            // Add other fields that need to be updated

            Manager updatedManager = managerRepository.save(existingManager);
            ManagerDto managerDto = Utils.mapManagerEntityToManagerDto(updatedManager);
            response.setStatusCode(200);
            response.setMessage("Profile updated successfully");
            response.setManager(managerDto);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating manager profile " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getManagerByEmail(String email) {
        Response response = new Response();

        try {
            Manager manager = managerRepository.findByEmail(email).orElseThrow(() -> new OurException("Manager Not Found"));
            ManagerDto managerDto = Utils.mapManagerEntityToManagerDto(manager);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setManager(managerDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting manager by email" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getManagerByName(String name) {
        Response response = new Response();

        try {
            Manager manager = managerRepository.findByName(name).orElseThrow(() -> new OurException("Manager Not Found"));
            ManagerDto managerDto = Utils.mapManagerEntityToManagerDto(manager);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setManager(managerDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting manager by name" + e.getMessage());
        }
        return response;
    }


    private void setManagerValues(ManagerDto managerDto) {
        if (managerDto == null) return;
        Long companyId = managerDto.getCompanyDtoId();
        Company company = companyRepository.findById(companyId).orElse(null);
        CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
        managerDto.setCompanyDto(companyDto);

        // Long managerId = employeeDto.getManagerDtoId();
        // Manager manager = null;
        // if (managerId != null) {
        //     manager = managerRepository.findById(managerId).orElse(null);
        // }
        
        // Empl managerDto = Utils.mapManagerEntityToManagerDto(manager);
        // managerDto.setManagerDto(managerDto);
        List<Long> employeeIdList = managerDto.getEmployeesId();
        List<Employee> employeeList = employeeRepository.findAllById(employeeIdList);
        List<EmployeeDto> employeeDtoList = Utils.mapEmployeeListEntityToEmployeeDtoList(employeeList);
        managerDto.setEmployees(employeeDtoList);

        List<Long> projectIdList = managerDto.getProjectsId();
        List<Project> projectList = projectRepository.findAllById(projectIdList);
        List<ProjectDto> projectDtoList = Utils.mapProjectListEntityToProjectDtoList(projectList);
        managerDto.setProjects(projectDtoList);
    }


    public boolean isValidManager(String email, String password) {
        Manager manager = managerRepository.findByEmail(email).orElse(null);
        if (manager == null) return false;
        return true;
    }
}
