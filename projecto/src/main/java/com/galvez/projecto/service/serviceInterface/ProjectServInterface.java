package com.galvez.projecto.service.serviceInterface;

import java.time.LocalDate;
import java.util.List;

import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Project;

public interface ProjectServInterface {
    Response addFinishedProject(String projectName, String projectAddress, String projectType, String projectDescription, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit);
    Response saveInitialProject(String projectName, String projectAddress, String projectType, String projectDescription, String resourceCost, String projectRevenue, String projectProfit);
    Response getProjectById(Long projectId);
    
    Response deleteProject(Long projectId);

    Response updateProject(Long projectId, String projectName, String projectDescription, String projectType, String projectAddress, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit);

    List<String> getAllProjectTypes();
    Project getProjectByTypeAndAddressAndDescription(String projectType, String projectAddress, String projectDescription);

    List<Project> getProjectsByNameAndTypeAndAddress(String projectName, String projectType, String projectAddress);
    List<Project> getProjectsByNameAndAddress(String projectName, String projectAddress);

    List<Project> getProjectsByTypeAndAddress(String projectType, String projectAddress);

    Response getAllProjects();
    List<Project> getProjectsByDateAndType(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType);
    List<Project> getProjectsByDateAndTypeAndAddress(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType, String projectAddress);

    Response getProjectsByEmployee(Long employeeId);
}
