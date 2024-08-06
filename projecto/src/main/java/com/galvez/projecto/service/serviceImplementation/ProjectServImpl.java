package com.galvez.projecto.service.serviceImplementation;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.galvez.projecto.dto.CompanyDto;
import com.galvez.projecto.dto.EmployeeDto;
import com.galvez.projecto.dto.ManagerDto;
import com.galvez.projecto.dto.ProjectDto;
import com.galvez.projecto.dto.Response;
import com.galvez.projecto.exception.OurException;
import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Employee;
import com.galvez.projecto.model.Manager;
import com.galvez.projecto.model.Project;
import com.galvez.projecto.utils.Utils;
import com.galvez.projecto.repository.CompanyRepository;
import com.galvez.projecto.repository.EmployeeRepository;
import com.galvez.projecto.repository.ManagerRepository;
import com.galvez.projecto.repository.ProjectRepository;
import com.galvez.projecto.service.serviceInterface.ProjectServInterface;

@Service
public class ProjectServImpl implements ProjectServInterface{
    
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ManagerRepository managerRepository;

    // @Autowired
    // private AwsS3Service awsS3Service;

    // Response addFinishedProject(String projectName, String projectAddress, String projectType, String projectDescription, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit);
    // Response saveInitialProject(String projectName, String projectAddress, String projectType, String projectDescription, String resourceCost, String projectRevenue, String projectProfit);
    // Response getProjectById(Long projectId);
    
    // Response deleteProject(Long projectId);

    // Response updateProject(Long projectId, String projectName, String projectDescription, String projectType, String projectAddress, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit);

    // List<String> getAllProjectTypes();
    // Project getProjectByTypeAndAddressAndDescription(String projectType, String projectAddress, String projectDescription);



    // Project getProjectByNameAndTypeAndAddress(String projectName, String projectType, String projectAddress);
    // Project getProjectByNameAndAddress(String projectName, String projectAddress);



    // List<Project> getProjectsByTypeAndAddress(String projectType, String projectAddress);

    // Response getAllProjects();
    // List<Project> getProjectsByDateAndType(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType);
    // List<Project> getProjectsByDateAndTypeAndAddress(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType, String projectAddress);
    

    @Override
    public Response addFinishedProject(String projectName, String projectAddress, String projectType, String projectDescription, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit) {

        Response response = new Response();
        try {
//            Project project = new Project();
//             String imageUrl = null;
//             if (photo != null) {
//                 imageUrl = awsS3Service.saveImageToS3(photo);
// //                project.setProjectPhotoUrl(imageUrl);
//             }

            Project project = projectRepository.findProjectByTypeAndAddress(projectType, projectAddress);
            System.out.println("project: " + project);

            
//            project.setProjectAddress(projectAddress);
//            project.setProjectType(projectType);
//            project.setProjectDescription(projectDescription);
//            project.setProjectDateStarted(projectDateStarted);
//            project.setProjectDateFinished(projectDateFinished);
            long resourceCostLong = Long.parseLong(resourceCost);
            long projectRevenueLong = Long.parseLong(projectRevenue);
            long projectProfitLong = Long.parseLong(projectProfit);

            projectRepository.updateFinishedProject(project.getId(), projectDateFinished, projectDateStarted, projectDescription, resourceCost, projectRevenue, projectProfit, projectName);
            // Project savedProject = projectRepository.save(project);
            // ProjectDto projectDto = Utils.mapProjectEntityToProjectDto(savedProject);
            response.setStatusCode(200);
            response.setMessage("Successful");
            // response.setProject(projectDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving a project " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response saveInitialProject(String projectName, String projectAddress, String projectType, String projectDescription, String resourceCost, String projectRevenue, String projectProfit) {
        Response response = new Response();
        try {
            Project project = new Project();
            project.setProjectName(projectName);
            project.setProjectAddress(projectAddress);
            project.setProjectType(projectType);
            project.setProjectDescription(projectDescription);

//            long resourceCostLong = Long.parseLong(resourceCost);
//            long projectRevenueLong = Long.parseLong(projectRevenue);
//            long projectProfitLong = Long.parseLong(projectProfit);


            projectRepository.saveProjectToDatabase(projectName, projectAddress, null, null, projectDescription, projectType, resourceCost, projectRevenue, projectProfit, project.getCompany().getId(), project.getEmployee().getId(), project.getManager().getId());
            ProjectDto projectDto = Utils.mapProjectEntityToProjectDto(project);
            response.setStatusCode(200);
            response.setMessage("Successful");  
            response.setProject(projectDto);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error saving an initial project " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getProjectById(Long projectId) {

        Response response = new Response();

        try {
            Project project = projectRepository.findById(projectId).orElseThrow(() -> new OurException("Project Not Found"));
            ProjectDto projectDto = Utils.mapProjectEntityToProjectDto(project);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setProject(projectDto);
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving project " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response deleteProject(Long projectId) {

        Response response = new Response();
        try {
            projectRepository.findById(projectId).orElseThrow(() -> new OurException("Project Not Found"));
            projectRepository.deleteById(projectId);
            response.setStatusCode(200);
            response.setMessage("Successful");
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error deleting project " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response updateProject(Long projectId, String projectName, String projectDescription, String projectType, String projectAddress, LocalDate projectDateStarted, LocalDate projectDateFinished, String resourceCost, String projectRevenue, String projectProfit) {
        Response response = new Response();
        try {
            // String imageUrl = null;
            // if (photo != null && !photo.isEmpty()) {
            //     imageUrl = awsS3Service.saveImageToS3(photo);
            // }
            Project project = projectRepository.findById(projectId).orElseThrow(() -> new OurException("Project Not Found"));
            if (!projectName.isEmpty()) project.setProjectName(projectName);
            if (!projectType.isEmpty()) project.setProjectType(projectType);
            if (!projectAddress.isEmpty()) project.setProjectAddress(projectAddress);
            if (!projectDescription.isEmpty()) project.setProjectDescription(projectDescription);
            // if (imageUrl != null) project.setProjectPhotoUrl(imageUrl);
            if (projectDateStarted != null) project.setProjectDateStarted(projectDateStarted);
            if (projectDateFinished != null) project.setProjectDateFinished(projectDateFinished);
            
            long resourceCostLong = 0;
            if (!resourceCost.isEmpty()) {
                project.setResourceCost(resourceCost);
                resourceCostLong = Long.parseLong(resourceCost);
            }

            long projectRevenueLong = 0;
            if (!projectRevenue.isEmpty()) {
                project.setProjectRevenue(projectRevenue);
                projectRevenueLong = Long.parseLong(projectRevenue);
            }

            long projectProfitLong = 0;
            if (!projectProfit.isEmpty()) {
                project.setProjectProfit(projectProfit);
                projectProfitLong = Long.parseLong(projectProfit);
            }
            
            // projectRepository.updateProject(project.getId(), projectName, projectDescription, projectType, projectAddress, projectDateStarted, projectDateFinished, resourceCostLong, projectRevenueLong, projectProfitLong);
            projectRepository.updateProject(project.getId(), project.getProjectName(), project.getProjectDescription(), project.getProjectType(), project.getProjectAddress(), project.getProjectDateStarted(), project.getProjectDateFinished(), resourceCost, projectRevenue, projectProfit);
            ProjectDto projectDto = Utils.mapProjectEntityToProjectDto(project);

            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setProject(projectDto);
        }
        catch (OurException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error updating project " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<String> getAllProjectTypes() {
        return projectRepository.findDistinctProjectTypes();
    }


    @Override
    public Project getProjectByTypeAndAddressAndDescription(String projectType, String projectAddress, String projectDescription) {
        return projectRepository.findProjectByTypeAndAddressAndDescription(projectType, projectAddress, projectDescription);
    }

    @Override
    public List<Project> getProjectsByNameAndTypeAndAddress(String projectName, String projectType, String projectAddress) {
        return projectRepository.findProjectsByNameAndTypeAndAddress(projectName, projectType, projectAddress);
    }

    @Override
    public List<Project> getProjectsByNameAndAddress(String projectName, String projectAddress) {
        return projectRepository.findProjectsByNameAndAddress(projectName, projectAddress);
    }

    @Override
    public List<Project> getProjectsByTypeAndAddress(String projectType, String projectAddress) {
        return projectRepository.findProjectsByTypeAndAddress(projectType, projectAddress);
    }

    @Override
    public Response getAllProjects() {

        Response response = new Response();
        try {
            List<Project> projectList = projectRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            List<ProjectDto> projectDtoList = Utils.mapProjectListEntityToProjectDtoList(projectList);
            projectDtoList.forEach(this::setProjectValues);
            response.setStatusCode(200);
            response.setMessage("Successful");
            response.setProjectList(projectDtoList);
        }
        catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error retrieving projects " + e.getMessage());
        }
        return response;
    }

    @Override
    public List<Project> getProjectsByDateAndType(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType) {
        return projectRepository.findProjectsByDateAndType(projectDateStarted, projectDateFinished, projectType);
    }

    @Override
    public List<Project> getProjectsByDateAndTypeAndAddress(LocalDate projectDateStarted, LocalDate projectDateFinished, String projectType, String projectAddress) {
        return projectRepository.findProjectsByDateAndTypeAndAddress(projectDateStarted, projectDateFinished, projectType, projectAddress);
    }


    private void setProjectValues(ProjectDto projectDto) {
        List<Long> employeeIdList = projectDto.getEmployeesId();
        List<Employee> employeeList = employeeRepository.findAllById(employeeIdList);
        List<EmployeeDto> employeeDtolist = Utils.mapEmployeeListEntityToEmployeeDtoList(employeeList);
        projectDto.setEmployees(employeeDtolist);

        Long companyId = projectDto.getCompanyDtoId();
        Company company = companyRepository.findById(companyId).orElse(null);
        CompanyDto companyDto = Utils.mapCompanyEntityToCompanyDto(company);
        projectDto.setCompanyDto(companyDto);

        Long managerId = projectDto.getManagerDtoId();
        Manager manager = null;
        ManagerDto managerDto = null;
        if (managerId != null) {
            manager = managerRepository.findById(managerId).orElse(null);
            managerDto = Utils.mapManagerEntityToManagerDto(manager);
        }
        projectDto.setManagerDto(managerDto);
    }
}
