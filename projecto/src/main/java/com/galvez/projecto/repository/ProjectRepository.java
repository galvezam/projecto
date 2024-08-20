package com.galvez.projecto.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Project;

import jakarta.transaction.Transactional;

public interface ProjectRepository extends JpaRepository<Project, Long>{

    @Query("SELECT DISTINCT p.projectType FROM Project p")
    List<String> findDistinctProjectTypes();

    @Query("SELECT p FROM Project p WHERE p.projectType = :projectType AND p.projectAddress = :projectAddress AND p.projectDescription = :projectDescription")
    Project findProjectByTypeAndAddressAndDescription(String projectType, String projectAddress, String projectDescription);

    @Query("SELECT p FROM Project p WHERE p.projectName = :projectName AND p.projectType = :projectType AND p.projectAddress = :projectAddress AND p.projectDescription = :projectDescription")
    Project findProjectByNameAndTypeAndAddressAndDescription(String projectName, String projectType, String projectAddress, String projectDescription);

//    @Query("SELECT p FROM Project p WHERE p.projectType = :projectType AND p.projectDateStarted >= :projectDateStarted AND p.projectDateFinished <= :projectDateFinished")
//    List<Project> findProjectsByDateAndType(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType);

    @Query(value=("SELECT * FROM projects WHERE project_type = :projectType AND project_date_started >= :projectDateStarted AND project_date_finished <= :projectDateFinished"), nativeQuery = true)
    List<Project> findProjectsByDateAndType(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType);

    @Query("SELECT p FROM Project p WHERE p.projectType = :projectType AND p.projectAddress = :projectAddress AND p.projectDateStarted >= :projectDateStarted AND p.projectDateFinished <= :projectDateFinished")
    List<Project> findProjectsByDateAndTypeAndAddress(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType, String projectAddress);
    
//    @Query("SELECT p FROM Project p WHERE p.id NOT IN (SELECT b.projectId FROM Booking b)")
//    List<Project> getAllAvailableProjects();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO projects (project_address, project_date_finished, project_date_started, project_description, project_name, project_profit, project_revenue, project_type, resource_cost, companys_id, employees_id, managers_id) VALUES (:projectName, :projectAddress, :projectDateFinished, :projectDateStarted, :projectDescription, :projectType, :resourceCost, :projectRevenue, :projectProfit, :companysId, :employeesId, :managersId)", nativeQuery = true)
    void saveProjectToDatabase(String projectName, String projectAddress, LocalDate projectDateFinished, LocalDate projectDateStarted, String projectDescription, String projectType, String resourceCost, String projectRevenue, String projectProfit, Long companysId, Long employeesId, Long managersId);


    @Query("SELECT p FROM Project p WHERE p.projectType = :projectType AND p.projectAddress = :projectAddress")
    Project findProjectByTypeAndAddress(String projectType, String projectAddress);


    @Modifying
    @Transactional
    @Query(value = "UPDATE projects p SET p.project_date_finished = :projectDateFinished, p.project_date_started = :projectDateStarted, p.project_description = :projectDescription, p.resource_cost = :resourceCost, p.project_revenue = :projectRevenue, p.project_profit = :projectProfit, p.project_name = :projectName WHERE p.id = :projectId", nativeQuery = true)
    void updateFinishedProject(Long projectId, LocalDate projectDateFinished, LocalDate projectDateStarted, String projectDescription, String resourceCost, String projectRevenue, String projectProfit, String projectName);

    @Modifying
    @Transactional
    @Query(value = "UPDATE projects p SET p.project_name = :projectName, p.project_description = :projectDescription, p.project_type = :projectType, p.project_address = :projectAddress, p.project_date_started = :projectDateStarted, p.project_date_finished = :projectDateFinished, p.resource_cost = :resourceCost, p.project_revenue = :projectRevenue, p.project_profit = :projectProfit WHERE p.id = :projectId", nativeQuery = true)
    void updateProject(Long projectId, String projectName, String projectDescription, String projectType, String projectAddress, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit);
    // public Response updateProject(Long projectId, String projectName, String projectDescription, String projectType, String projectAddress, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit) {


    @Query("SELECT p FROM Project p WHERE p.projectName = :projectName AND p.projectType = :projectType AND p.projectAddress = :projectAddress")
    List<Project> findProjectsByNameAndTypeAndAddress(String projectName, String projectType, String projectAddress);

    @Query("SELECT p FROM Project p WHERE p.projectName = :projectName AND p.projectAddress = :projectAddress")
    List<Project> findProjectsByNameAndAddress(String projectName, String projectAddress);

    @Query("SELECT p FROM Project p WHERE p.projectType = :projectType AND  p.projectAddress = :projectAddress")
    List<Project> findProjectsByTypeAndAddress(String projectType, String projectAddress);

    @Query("SELECT p FROM Project p WHERE p.employee.id = :employeeId")
    List<Project> findProjectsByEmployee(Long employeeId);

    @Query("SELECT p FROM Project p WHERE p.id = (SELECT e.currentProject.id FROM Employee e WHERE e.id = :employeeId)")
    Project findCurrentProjectByEmployee(Long employeeId);
}
