package com.galvez.projecto.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Employee;

import jakarta.transaction.Transactional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
    
    boolean existsByEmail(String email);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByName(String name);

    // @Query("SELECT u FROM User u WHERE u.phoneNumber = :phoneNumber AND u.email = :email")
    // Optional<User> findByPhoneNumberAndEmail(String phoneNumber, String email);



    @Query("SELECT c FROM Company c WHERE c.id = (SELECT e.company.id FROM Employee e WHERE e.id = :employeeId)")
    Company findCompanyByEmployee(Long employeeId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO employees (email, name, password, phone_number, employee_date_started, total_revenue, total_revenue_after_tax, companys_id, managers_id) VALUES (:email, :name, :password, :phoneNumber, :dateStarted, :totalRevenue, :totalRevenueAfterTax, :companysId, :managersId)", nativeQuery = true)
    void saveEmployeeToDatabase(String email, String name, String password, String phoneNumber, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax, Long companysId, Long managersId);

    // @Modifying
    // @Transactional
    // @Query(value = "INSERT INTO employees (email, name, password, phone_number, manager_id, company_id, date_started, total_revenue, total_revenue_after_tax) VALUES (:email, :name, :password, :phoneNumber, :manager, :company, :dateStarted, :totalRevenue, :totalRevenueAfterTax)", nativeQuery = true)
    // void saveEmployeeToDatabase(String email, String name, String password, String phoneNumber, Long managerId, Long companyId, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax);

}
