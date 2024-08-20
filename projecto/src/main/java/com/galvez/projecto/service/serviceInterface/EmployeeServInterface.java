package com.galvez.projecto.service.serviceInterface;

import java.time.LocalDate;

import com.galvez.projecto.dto.LoginRequest;
import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Employee;
// import com.galvez.projecto.model.Manager;

public interface EmployeeServInterface {
    
    // Response register(User user);

    Response login(LoginRequest loginRequest);

    Response addEmployeeToDatabase(String email, String name, String password, String phoneNumber, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax, Long companysId, Long managersId);

    Response getAllEmployees();

    Response getManagerByEmployee(String employeeId);

    Response getCompanyByEmployee(Long employeeId);
    // Response getUserBookingHistory(String userId);

    Response deleteEmployee(String employeeId);

    Response getEmployeeById(Long employeeId);

    Response getMyInfo(String email);

    Response updateEmployeeProfile(Employee employee);

    Response getEmployeeByEmail(String email);
    Response getEmployeeByName(String name);
    
}
