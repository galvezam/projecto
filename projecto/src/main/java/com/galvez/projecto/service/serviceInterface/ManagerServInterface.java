package com.galvez.projecto.service.serviceInterface;

import java.time.LocalDate;

import com.galvez.projecto.dto.LoginRequest;
import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Manager;

public interface ManagerServInterface {
    

    // Response register(User user);

    // Response login(LoginRequest loginRequest);
    Response login(LoginRequest loginRequest);

    Response addManagerToDatabase(String email, String name, String password, String phoneNumber, String company, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax, Long companysId);
    
    Response getAllManagers();

    Response getEmployeesByManager(String managerId);

    Response getCompanyByManager(String managerId);

    Response deleteManager(String managerId);

    Response getManagerById(String managerId);

    Response getMyInfo(String email);

    Response updateManagerProfile(Manager manager);

    Response getManagerByEmail(String email);

    Response getManagerByName(String name);
    
}
