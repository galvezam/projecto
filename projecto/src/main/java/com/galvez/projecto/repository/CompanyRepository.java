package com.galvez.projecto.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.galvez.projecto.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Employee;
import com.galvez.projecto.model.Manager;

import jakarta.transaction.Transactional;

public interface CompanyRepository extends JpaRepository<Company, Long>{

    boolean existsByEmail(String email);
    boolean existsByName(String name);

    Optional<Company> findByEmail(String email);

//    @Query("SELECT c FROM Company c WHERE c.name = :name")
    Optional<Company> findByName(String name);

    @Query("SELECT e FROM Employee e WHERE e.id = :companyId")
    List<Employee> findEmployeesByCompany(Long companyId);

    @Query("SELECT m FROM Manager m WHERE m.id = :companyId")
    List<Manager> findManagersByCompany(Long companyId);

    @Query("SELECT p FROM Project p WHERE p.company.id = :companyId")
    List<Project> findProjectsByCompany(Long companyId);


    // private String totalRevenue;
    // private String totalProjectCosts;
    // private String totalProfit;
    // private String totalProfitAfterTax;
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO companys (email, name, password, phone_number, date_started, total_revenue, total_project_costs, total_profit, total_profit_after_tax) VALUES (:email, :name, :password, :phoneNumber, :dateStarted, :totalRevenue, :totalProjectCosts, :totalProfit, :totalProfitAfterTax)", nativeQuery = true)
    void saveCompanyToDatabase(String email, String name, String password, String phoneNumber, LocalDate dateStarted, String totalRevenue, String totalProjectCosts, String totalProfit, String totalProfitAfterTax);
    
}
