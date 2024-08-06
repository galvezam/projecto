package com.galvez.projecto.model;

import jakarta.persistence.*;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name is required")
    private String projectName;

    @NotEmpty(message = "Address is required")
    private String projectAddress;

    private String projectType;

    // private String projectPhotoUrl;

    private String projectDescription;

    private LocalDate projectDateStarted;
    private LocalDate projectDateFinished;

    private String resourceCost;
    private String projectRevenue;
    private String projectProfit;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companys_id")
    private Company company;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "managers_id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employees_id")
    private Employee employee;

    @OneToMany(mappedBy = "currentProject", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Employee> employeeList;
}