package com.galvez.projecto.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.galvez.projecto.dto.Response;
import com.galvez.projecto.model.Project;
import com.galvez.projecto.service.serviceInterface.ProjectServInterface;

@RestController
@RequestMapping("/projects")
public class ProjectController {
    
    @Autowired
    private ProjectServInterface projectService;

    // Response addFinishedProject(String projectName, String projectAddress, String projectType, String projectDescription, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit);
    // Response saveInitialProject(String projectName, String projectAddress, String projectType, String projectDescription, String resourceCost, String projectRevenue, String projectProfit);
    // Response getProjectById(Long projectId);
    
    // Response deleteProject(Long projectId);

    // Response updateProject(Long projectId, String projectName, String projectDescription, String projectType, String projectAddress, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit);

    // List<String> getAllProjectTypes();
    // Project getProjectByTypeAndAddressAndDescription(String projectType, String projectAddress, String projectDescription);

    // Project getProjectByNameAndTypeAndAddress(String projectName, String projectType, String projectAddress);
    // Project getProjectByNameAndTypeAndAddress(String projectName, String projectAddress);

    // List<Project> getProjectsByTypeAndAddress(String projectType, String projectAddress);

    // Response getAllProjects();
    // List<Project> getProjectsByDateAndType(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType);
    // List<Project> getProjectsByDateAndTypeAndAddress(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType, String projectAddress);

    @PostMapping("/add-project")
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> addProject(@RequestParam(value = "projectName", required = false) String projectName,
                                               @RequestParam(value = "projectAddress", required = false) String projectAddress,
                                               @RequestParam(value = "projectType", required = false) String projectType,
                                               @RequestParam(value = "projectDateStarted", required = false) String projectDateStarted,
                                               @RequestParam(value = "projectDateFinished", required = false) String projectDateFinished,
                                               @RequestParam(value = "projectDescription", required = false) String projectDescription,
                                               @RequestParam(value = "resourceCost", required = false) String resourceCost,
                                               @RequestParam(value = "projetRevenue", required = false) String projectRevenue,
                                               @RequestParam(value = "projectProfit", required = false) String projectProfit) {


        Response response = new Response();
        LocalDate projectDateStartedParsed = stringToDate(projectDateStarted);
        LocalDate projectDateFinishedParsed = stringToDate(projectDateFinished);

        // Project project = projectService.getProjectByTypeAndAddressAndDescription(projectType, projectAddress, null);

        try {
            // name, address, type , description, started, finsihed, resourceCost, projectRevenue, projectProft
            response = projectService.addFinishedProject(projectName, projectAddress, projectType, projectDescription, projectDateStartedParsed, projectDateFinishedParsed, resourceCost, projectRevenue, projectProfit);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error adding a finished project " + e.getMessage());
        }
//        Response projectResponse = projectService.getProjectById(project.getId());
//        ProjectDto projectDto = Utils.mapProjectEntityToProjectDto(project);
//        Response response = null;
//        if (projectDto.equals(projectResponse.getProject())) {
//            response = projectService.addFinishedProject(photo, projectDto.getProjectAddress(), projectDto.getProjectType(), projectDescription, projectDateStartedParsed, projectDateFinishedParsed);
//        }

        response.setMessage("Project added successfully");

        System.out.println("Project added successfully");
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/all-projects")
    public ResponseEntity<Response> getAllProjects() {
        Response response = projectService.getAllProjects();
        System.out.println(response);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/types")
    public List<String> getProjectTypes() {
        return projectService.getAllProjectTypes();
    }


    @PutMapping("/update/{projectId}")
    // @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> updateProject(
            @PathVariable Long projectId,
            @RequestParam(value = "projectAddress", required = false) String projectAddress,
            @RequestParam(value = "projectType", required = false) String projectType,
            @RequestParam(value = "projectDescription", required = false) String projectDescription,
            @RequestParam(value = "projectDateStarted", required = false) String projectDateStarted,
            @RequestParam(value = "projectDateFinished", required = false) String projectDateFinished,
            @RequestParam(value = "projectName", required = false) String projectName,
            @RequestParam(value = "resourceCost", required = false) String resourceCost,
            @RequestParam(value = "projectRevenue", required = false) String projectRevenue,
            @RequestParam(value = "projectProfit", required = false) String projectProfit
    ) {

        LocalDate projectDateStartedParsed = stringToDate(projectDateStarted);
        LocalDate projectDateFinishedParsed = stringToDate(projectDateStarted);
        // projectId, projectName, projectDescription, projectType, projectAddress, projectDateStarted, projectDateFinished, resourceCost, projectRevenue, projectProfit
        Response response = projectService.updateProject(projectId, projectName, projectDescription, projectType, projectAddress, projectDateStartedParsed, projectDateFinishedParsed, resourceCost, projectRevenue, projectProfit);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{projectId}")
    // @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<Response> deleteProject(@PathVariable Long projectId) {
        Response response = projectService.deleteProject(projectId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/project-id/{projectId}")
    public ResponseEntity<Response> getProjectById(@PathVariable String projectId) {
        Long id = Long.parseLong(projectId);
        Response response = projectService.getProjectById(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @GetMapping("/projects-by-date-and-type")
    public List<Project> getProjectsByDateAndType(@RequestParam(value = "projectDateStarted", required = false) String projectDateStarted,
                                                  @RequestParam(value = "projectDateFinished", required = false) String projectDateFinished,
                                                  @RequestParam(value = "projectType", required = false) String projectType,
                                                  @RequestParam(value = "projectAddress", required = false) String projectAddress) {
        LocalDate projectDateStartedParsed = stringToDate(projectDateStarted);
        LocalDate projectDateFinishedParsed = stringToDate(projectDateFinished);
        if (projectAddress != null) {
            return projectService.getProjectsByDateAndTypeAndAddress(projectDateStartedParsed, projectDateFinishedParsed, projectType, projectAddress);
        }
        
        return projectService.getProjectsByDateAndType(projectDateStartedParsed, projectDateFinishedParsed, projectType);
    }


    private LocalDate stringToDate(String date) {
        if (date == null) {
            return null;
        }
        LocalDate newDate;
        try {
            Instant instantStart = Instant.parse(date);
            newDate = LocalDate.ofInstant(instantStart, ZoneId.systemDefault());
        } catch (DateTimeParseException e) {
            // Handle the exception, maybe log it or rethrow it
            throw new IllegalArgumentException("Invalid date format: " + date, e);
        }
        return newDate;
    }


    @GetMapping("/projects-by-employee")
    public ResponseEntity<Response> getProjectsByEmployee(@RequestParam String employeeId) {
        System.out.println(employeeId);
        Long id = Long.parseLong(employeeId);
        Response response = projectService.getProjectsByEmployee(id);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

}
