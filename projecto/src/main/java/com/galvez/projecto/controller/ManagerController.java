package com.galvez.projecto.controller;

import java.time.LocalDate;
import java.util.List;

import com.galvez.projecto.dto.ManagerDto;
import com.galvez.projecto.model.Company;
import com.galvez.projecto.service.serviceInterface.CompanyServInterface;
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
import com.galvez.projecto.model.Manager;
import com.galvez.projecto.service.serviceInterface.ManagerServInterface;

@RestController
@RequestMapping("/managers")
public class ManagerController {
    
    @Autowired
    private ManagerServInterface managerService;

    @Autowired
    private CompanyServInterface companyService;

    @PostMapping("/add-manager")
    // @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Response> addManager(@RequestParam String email,
                                                @RequestParam String name,
                                                @RequestParam String password,
                                                @RequestParam String phoneNumber,
                                                // @RequestParam String manager,
                                                @RequestParam String company) {
        LocalDate dateStarted = LocalDate.now();
        String totalRevenue = "0";
        String totalRevenueAfterTax = "0";
        Response companyResponse = companyService.getCompanyByName(company);

        Response response = managerService.addManagerToDatabase(email, name, password, phoneNumber, company, dateStarted, totalRevenue, totalRevenueAfterTax, companyResponse.getCompany().getId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/all")
    public List<ManagerDto> getAllManagers() {
        Response response = managerService.getAllManagers();
        return response.getManagerList();
//        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
    // @PreAuthorize("hasAuthority('COMPANY')")
//    public ResponseEntity<Response> getAllManagers() {
//        Response response = managerService.getAllManagers();
//        return ResponseEntity.status(response.getStatusCode()).body(response);
//    }

    @GetMapping("/employees")
    // @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> getEmployeesByManager(@RequestParam String managerId) {
        Response response = managerService.getEmployeesByManager(managerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/company")
    // @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> getCompanyByManager(@RequestParam String managerId) {
        Response response = managerService.getCompanyByManager(managerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping
    // @PreAuthorize("hasAuthority('COMPANY')")
    public ResponseEntity<Response> deleteManager(@RequestParam String managerId) {
        Response response = managerService.deleteManager(managerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/")
    public ResponseEntity<Response> getMangerById(@RequestParam String managerId) {
        Response response = managerService.getManagerById(managerId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/email")
    public ResponseEntity<Response> getManagerByEmail(@RequestParam String email) {
        Response response = managerService.getManagerByEmail(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/name")
    public ResponseEntity<Response> getManagerByName(@RequestParam String name) {
        Response response = managerService.getManagerByName(name);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }


    @GetMapping("/info")
    public ResponseEntity<Response> getManagerInfo(@RequestParam String email) {
        Response response = managerService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PutMapping("/update-profile")
    public ResponseEntity<Response> updateManagerProfile(@RequestBody Manager manager) {
        Response response = managerService.updateManagerProfile(manager);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
