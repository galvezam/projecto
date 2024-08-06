package com.galvez.projecto.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Employee;
import com.galvez.projecto.model.Manager;

import jakarta.transaction.Transactional;

public interface ManagerRepository extends JpaRepository<Manager, Long>{
    
    boolean existsByEmail(String email);
    Optional<Manager> findByEmail(String email);
    Optional<Manager> findByName(String name);

    @Query("SELECT e FROM Employee e WHERE e.id = :managerId")
    List<Employee> findEmployeesByManager(Long managerId);

    @Query("SELECT c FROM Company c WHERE c.id IN (SELECT m.id FROM Manager m WHERE m.id = :managerId)")
    Company findCompanyByManager(Long managerId);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO managers (email, name, password, phone_number, company, date_started, total_revenue, total_revenue_after_tax, companys_id) VALUES (:email, :name, :password, :phoneNumber, :company, :dateStarted, :totalRevenue, :totalRevenueAfterTax, :companysId)", nativeQuery = true)
    void saveManagerToDatabase(String email, String name, String password, String phoneNumber, String company, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax, Long companysId);

    // @Modifying
    // @Transactional
    // @Query(value = "INSERT INTO managers (email, name, password, phone_number, company_id, date_started, total_revenue, total_revenue_after_tax) VALUES (:email, :name, :password, :phoneNumber, :company, :dateStarted, :totalRevenue, :totalRevenueAfterTax)", nativeQuery = true)
    // void saveManagerToDatabase(String email, String name, String password, String phoneNumber, Long companyId, LocalDate dateStarted, String totalRevenue, String totalRevenueAfterTax);

}
