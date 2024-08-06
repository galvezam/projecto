package com.galvez.projecto.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Manager;
import com.galvez.projecto.service.serviceInterface.CompanyServInterface;

@RestController
@RequestMapping("/companys")
public class CompanyController {
    
    @Autowired
    private CompanyServInterface companyService;


    @PostMapping("/add-company")
    // @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Response> addCompany(@RequestParam String email,
                                                @RequestParam String name,
                                                @RequestParam String password,
                                                @RequestParam String phoneNumber,
                                                // @RequestParam String manager,
                                                @RequestParam String company) {
        LocalDate dateStarted = LocalDate.now();
        String totalRevenue = "0";
        String totalProfit = "0";
        String totalProfitAfterTax = "0";
        String totalProjectCosts = "0";
        // add licensing to company
        Response response = companyService.addCompanyToDatabase(email, name, password, phoneNumber, dateStarted, totalRevenue, totalProjectCosts, totalProfit, totalProfitAfterTax);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    // @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Response> getAllCompanies() {
        Response response = companyService.getAllCompanies();
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

//    @GetMapping("/manager")
//    // @PreAuthorize("hasAuthority('MANAGER')")
//    public ResponseEntity<Response> getCompanyByManager(@RequestParam String managerId) {
//        Response response = companyService.getCompanyByManager(managerId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }
//
//    @GetMapping("/employee")
//    // @PreAuthorize("hasAuthority('MANAGER')")
//    public ResponseEntity<Response> getCompanyByEmployee(@RequestParam String employeeId) {
//        Response response = companyService.getCompanyByEmployee(employeeId);
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }

    // MAYBE CONVERT TO LIST<MANAGER>
    @GetMapping("/managers")
    // @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> getManagersByCompany(@RequestParam String companyId) {
        Response response = companyService.getManagersByCompany(companyId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    // MAYBE CONVERT TO LIST<EMPLOYEE>
    @GetMapping("/employees")
    // @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> getEmployeesByCompany(@RequestParam String companyId) {
        Response response = companyService.getEmployeesByCompany(companyId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping
    // @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Response> deleteCompany(@RequestParam String companyId) {
        Response response = companyService.deleteCompany(companyId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/")
    public ResponseEntity<Response> getCompanyById(@RequestParam String companyId) {
        Response response = companyService.getCompanyById(companyId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/email")
    public ResponseEntity<Response> getCompanyByEmail(@RequestParam String email) {
        Response response = companyService.getCompanyByEmail(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/name")
    public ResponseEntity<Response> getCompanyByName(@RequestParam String name) {
        Response response = companyService.getCompanyByName(name);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/info")
    public ResponseEntity<Response> getCompanyInfo(@RequestParam String email) {
        Response response = companyService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Response> updateCompanyProfile(@RequestBody Company company) {
        Response response = companyService.updateCompanyProfile(company);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }




}
