package com.galvez.projecto.service.serviceImplementation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.galvez.projecto.service.serviceInterface.EmployeeServInterface;

@Service
public class EmployeeServImpl implements EmployeeServInterface {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ManagerRepository managerRepository;

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

            System.out.println("before authenticationManager ");
//            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            System.out.println("after authenticationManager ");
            
            System.out.println("loginRequest " + loginRequest);

            Employee employee = employeeRepository.findByEmail(loginRequest.getEmail()).orElseThrow(() -> new OurException("User Not Found"));
            UserDetails userDetails = jwtUtils.createEmployeeUserDetails(employee.getEmail());
            System.out.println("user details: "+userDetails.getAuthorities());
            var token = jwtUtils.generateToken(userDetails);
            System.out.println("token: "+token);
            EmployeeDto employeeDto = Utils.mapEmployeeEntityToEmployeeDto(employee);
            System.out.println(employeeDto);
            Company company = employeeRepository.findCompanyByEmployee(employee.getId());

            CompanyDto companyDto = null;
            if (company != null) {
                companyDto = Utils.mapCompanyEntityToCompanyDto(company);
            }

            response.setEmployee(employeeDto);
            response.setCompany(companyDto);
            response.setStatusCode(200);
            response.setToken(token);
            response.setRole(new Employee().getRole());
            response.setExpirationTime("7 days");
            response.setMessage("Successful");
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
//            response.setMessage("Invalid Username/Password");
             response.setMessage("Error occurred during user login " + e.getMessage());
        }
        return response;
    }

    // SAVES EMPLOYEE TO DATABASE
    @Override
    public Response addEmployeeToDatabase(String email, String name, String password, String phoneNumber, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax, Long companysId, Long managersId) {
        Response response = new Response();

        try {
            Employee employee = null;
            if (employeeRepository.existsByEmail(email)) {
                throw new OurException(email + " already exists.");
            }
            else {
                employee = new Employee();
                employee.setEmail(email);
            }
            if (password == null) {
                // user.setPassword(passwordEncoder.encode("abc"));
                System.out.println("password is null");
            }
            else {
                employee.setPassword(passwordEncoder.encode(password));
            }
            employee.setPhoneNumber(phoneNumber);
            employee.setName(name);
            employee.setEmployeeDateStarted(dateStarted);
            employee.setTotalRevenue(totalRevenue);
            employee.setTotalRevenueAfterTax(totalRevenueAfterTax);
            

            // long totalRevenueLong = Long.parseLong(totalRevenue);
            // long totalRevenueAfterTaxLong = Long.parseLong(totalRevenueAfterTax);

            employeeRepository.saveEmployeeToDatabase(employee.getEmail(), employee.getName(), employee.getPassword(), employee.getPhoneNumber(), employee.getEmployeeDateStarted(), employee.getTotalRevenue(), employee.getTotalRevenueAfterTax(), companysId, managersId);
            // employeeRepository.saveEmployeeToDatabase(employee);
            response.setStatusCode(200);
            response.setMessage("Successful");
            // response.setCompanyList(companyDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving employee to database " + e.getMessage());
        }

        return response;
    }

    // GET ALL EMPLOYEES
    @Override
    public Response getAllEmployees() {
        Response response = new Response();

        try {
            List<Employee> employeeList = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<EmployeeDto> employeeDtoList = Utils.mapEmployeeListEntityToEmployeeDtoList(employeeList);
            // employeeDtoList.stream().map(EmployeeServImpl::setEmployeeValues).collect(Collectors.toList());
            employeeDtoList.forEach(this::setEmployeeValues);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployeeList(employeeDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving employees " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getManagerByEmployee(String employeeId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(employeeId);
            Employee employee = employeeRepository.getReferenceById(id);
            EmployeeDto employeeDto = Utils.mapEmployeeEntityToEmployeeDto(employee);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployee(employeeDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving manger from employee " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getCompanyByEmployee(Long employeeId) {
        Response response = new Response();

        try {
//            Long id = Long.valueOf(employeeId);

            Company company = employeeRepository.findCompanyByEmployee(employeeId);

            CompanyDto companyDto = null;
            if (company != null) {
                companyDto = Utils.mapCompanyEntityToCompanyDto(company);
                setCompanyValues(companyDto);
            }

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setCompany(companyDto);

        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving company from employee " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response deleteEmployee(String employeeId) {
        Response response = new Response();

        try {
            Long id = Long.valueOf(employeeId);
            employeeRepository.findById(id).orElseThrow(() -> new OurException("Employee Not Found"));
            employeeRepository.deleteById(id);
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
    public Response getEmployeeById(Long employeeId) {
        Response response = new Response();

        try {
//            Long id = Long.valueOf(employeeId);
            Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new OurException("Employee Not Found"));
            EmployeeDto employeeDto = Utils.mapEmployeeEntityToEmployeeDto(employee);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployee(employeeDto);
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving project " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String email) {
        
        Response response = new Response();

        try {
            Employee employee = employeeRepository.findByEmail(email).orElseThrow(() -> new OurException("Employee Not Found"));
            EmployeeDto employeeDto = Utils.mapEmployeeEntityToEmployeeDto(employee);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployee(employeeDto);
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting employee information." + e.getMessage());
        }
        return response;

    }

    @Override
    public Response updateEmployeeProfile(Employee employee) {
        Response response = new Response();

        try {
            // Long id = employee.getId();
            
            Employee existingEmployee = employeeRepository.findById(employee.getId())
                    .orElseThrow(() -> new OurException("User Not Found"));

            existingEmployee.setName(employee.getName());
            existingEmployee.setEmail(employee.getEmail());
            // existingEmployee.setRole(user.getRole());
            // Add other fields that need to be updated

            Employee updatedEmployee = employeeRepository.save(existingEmployee);
            EmployeeDto employeeDto = Utils.mapEmployeeEntityToEmployeeDto(updatedEmployee);
            response.setStatusCode(200);
            response.setMessage("Profile updated successfully");
            response.setEmployee(employeeDto);
        } catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating employee profile " + e.getMessage());
        }

        return response;
    }

    @Override
    public Response getEmployeeByEmail(String email) {
        Response response = new Response();

        try {
            Employee employee = employeeRepository.findByEmail(email).orElseThrow(() -> new OurException("Employee Not Found"));
            EmployeeDto employeeDto = Utils.mapEmployeeEntityToEmployeeDto(employee);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployee(employeeDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting employee by email" + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getEmployeeByName(String name) {
        Response response = new Response();

        try {
            Employee employee = employeeRepository.findByName(name).orElseThrow(() -> new OurException("Employee Not Found"));
            EmployeeDto employeeDto = Utils.mapEmployeeEntityToEmployeeDto(employee);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setEmployee(employeeDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error getting employee by name" + e.getMessage());
        }
        return response;
    }

    private void setEmployeeValues(EmployeeDto employeeDto) {
        
        Long companyId = employeeDto.getCompanyDtoId();
        Company company = companyRepository.findById(companyId).orElse(null);
        CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
        employeeDto.setCompanyDto(companyDto);

        Long managerId = employeeDto.getManagerDtoId();
        Manager manager = null;
        if (managerId != null) {
            manager = managerRepository.findById(managerId).orElse(null);
        }
        
        ManagerDto managerDto = Utils.mapManagerEntityToManagerDto(manager);
        employeeDto.setManagerDto(managerDto);

        List<Long> projectIdList = employeeDto.getProjectsId();
        List<Project> projectList = projectRepository.findAllById(projectIdList);
        List<ProjectDto> projectDtoList = Utils.mapProjectListEntityToProjectDtoList(projectList);
        employeeDto.setProjects(projectDtoList);
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

    public boolean isValidEmployee(String email, String password) {
        Employee employee = employeeRepository.findByEmail(email).orElse(null);
        if (employee == null) return false;
        return true;
    }
    


}