package com.galvez.projecto.controller;

import java.time.LocalDate;

import com.galvez.projecto.service.serviceInterface.CompanyServInterface;
import com.galvez.projecto.service.serviceInterface.ManagerServInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Employee;
import com.galvez.projecto.service.serviceInterface.EmployeeServInterface;

@RestController
@RequestMapping("/employees")
public class EmployeeController {
    
    @Autowired
    private EmployeeServInterface employeeService;

    @Autowired
    private ManagerServInterface managerService;

    @Autowired
    private CompanyServInterface companyService;


    @GetMapping("/all")
    // @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllEmployees() {
        Response response = employeeService.getAllEmployees();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/add-employee")
    // @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> addEmployee(@RequestParam String email,
                                                @RequestParam String name,
                                                @RequestParam String password,
                                                @RequestParam String phoneNumber,
                                                @RequestParam String manager,
                                                @RequestParam String company) {
        LocalDate dateStarted = LocalDate.now();
        String totalRevenue = "0";
        String totalRevenueAfterTax = "0";
        Response managerResponse = managerService.getManagerByName(manager);
        Response companyResponse = companyService.getCompanyByName(company);
        Response response = employeeService.addEmployeeToDatabase(email, name, password, phoneNumber, dateStarted, totalRevenue, totalRevenueAfterTax, companyResponse.getCompany().getId(), managerResponse.getManager().getId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/manager")
    public ResponseEntity<Response> getManagerByEmployee(@RequestParam String employeeId) {
        Response response = employeeService.getManagerByEmployee(employeeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/company")
    public ResponseEntity<Response> getCompanyByEmployee(@RequestParam String employeeId) {
        System.out.println("emp id " + employeeId);
        Response response = employeeService.getCompanyByEmployee(employeeId);
        System.out.println(response);
        System.out.println(response.getCompany());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping
    public ResponseEntity<Response> deleteEmployee(@RequestParam String employeeId) {
        Response response = employeeService.deleteEmployee(employeeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/id")
    public ResponseEntity<Response> getEmployeeById(@RequestParam String employeeId) {
        Response response = employeeService.getEmployeeById(employeeId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/email")
    public ResponseEntity<Response> getEmployeeByEmail(@RequestParam String email) {
        Response response = employeeService.getEmployeeByEmail(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/name")
    public ResponseEntity<Response> getEmployeeByName(@RequestParam String name) {
        Response response = employeeService.getEmployeeByName(name);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/info")
    public ResponseEntity<Response> getEmployeeInfo(@RequestParam String email) {
        Response response = employeeService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Response> updateEmployeeProfile(@RequestBody Employee employee) {
        Response response = employeeService.updateEmployeeProfile(employee);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


}
