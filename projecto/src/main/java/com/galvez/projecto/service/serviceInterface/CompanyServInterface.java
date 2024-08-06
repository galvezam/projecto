package com.galvez.projecto.service.serviceInterface;

import java.time.LocalDate;

import com.galvez.projecto.dto.LoginRequest;
import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Company;

public interface CompanyServInterface {
    

    Response login(LoginRequest loginRequest);
    
    Response addCompanyToDatabase(String email, String name, String password, String phoneNumber, LocalDate dateStarted, String totalRevenue, String totalProjectCosts, String totalProfit, String totalProfitAfterTax);

    Response getAllCompanies();

//    Response getCompanyByManager(String managerId);
//    Response getCompanyByEmployee(String employeeId);

    Response getManagersByCompany(String companyId);
    Response getEmployeesByCompany(String companyId);
    // Response getUserBookingHistory(String userId);

    Response deleteCompany(String companyId);

    Response getCompanyById(String companyId);

    Response getMyInfo(String email);

    Response updateCompanyProfile(Company company);

    Response getCompanyByEmail(String email);

    Response getCompanyByName(String name);

}
