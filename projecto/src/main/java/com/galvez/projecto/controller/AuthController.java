package com.galvez.projecto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galvez.projecto.dto.LoginRequest;
import com.galvez.projecto.dto.Response;
import com.galvez.projecto.dto.SignupRequest;
import com.galvez.projecto.service.CustomerUserDetailsService;
import com.galvez.projecto.service.serviceImplementation.CompanyServImpl;
import com.galvez.projecto.service.serviceImplementation.EmployeeServImpl;
import com.galvez.projecto.service.serviceImplementation.ManagerServImpl;
import com.galvez.projecto.utils.JWTUtils;

import java.time.LocalDate;

@RestController
@RequestMapping("/auth")
public class AuthController {
    

    private final JWTUtils jwtUtils;

    private final CustomerUserDetailsService userDetailsService;

    @Autowired
    private CompanyServImpl companyService;

    @Autowired
    private ManagerServImpl managerService;

    @Autowired
    private EmployeeServImpl employeeService;

    public AuthController(JWTUtils jwtUtils, CustomerUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // Implement logic to authenticate user

        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        if (employeeService.isValidEmployee(email, password)) {
            // Employee login successful
            // Generate and return JWT token for employee
            // String jwtToken = employeeService.generateEmployeeToken(email);
            Response employeeResponse = employeeService.login(loginRequest);
            return ResponseEntity.status(employeeResponse.getStatusCode()).body(employeeResponse);
        } 
        // Check if the user is a company admin
        else if (companyService.isValidCompany(email, password)) {
            // Company admin login successful
            // Generate and return JWT token for company admin
            Response companyResponse = companyService.login(loginRequest);
            return ResponseEntity.status(companyResponse.getStatusCode()).body(companyResponse);
        } 
        // Check if the user is a manager
        else if (managerService.isValidManager(email, password)) {
            // Manager login successful
            // Generate and return JWT token for manager
            Response managerRepsonse = managerService.login(loginRequest);
            return ResponseEntity.status(managerRepsonse.getStatusCode()).body(managerRepsonse);
        } 
        else {
            // Handle invalid login attempts
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        
        // if (userDetails != null && jwtUtils.validateToken(loginRequest.getToken(), userDetails)) {
        //     String jwtToken = jwtUtils.generateToken(userDetails);
        //     return ResponseEntity.ok(jwtToken);
        // } else {
        //     return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        // }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest signupRequest) {
        // Implement logic to register a new employee or company
        // You can access signup details like signupRequest.getUsername(), signupRequest.getPassword(), etc.
        
        String email = signupRequest.getEmail();
        String password = signupRequest.getPassword();
        String phoneNumber = signupRequest.getPhoneNumber();
        String name = signupRequest.getName();

        LocalDate dateStarted = LocalDate.now();
        Response response = new Response();
        Long companyId = (long) -1;
        Long managerId = (long) -1;
        if (signupRequest.getRole().equals("Employee")) {
            response = employeeService.addEmployeeToDatabase(email, name, password, phoneNumber, dateStarted, "0", "0", companyId, managerId);

        }
        else if (signupRequest.getRole().equals("Company")) {
            response = companyService.addCompanyToDatabase(email, name, password, phoneNumber, dateStarted, "0", "0", "0", "0");
        } 

        // Perform registration logic here
        
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    // @PostMapping("/register")
    // public ResponseEntity<Response> register(@RequestBody User user) {
    //     Response response = userService.register(user);
    //     return ResponseEntity.status(response.getStatusCode()).body(response);
    // }

    // @PostMapping("/login")
    // public ResponseEntity<Response> login(@RequestBody LoginRequest loginRequest) {
    //     System.out.println(loginRequest);
    //     if (loginRequest)
    //     Response response = employeeService.login(loginRequest);
    //     return ResponseEntity.status(response.getStatusCode()).body(response);
    // }
}
