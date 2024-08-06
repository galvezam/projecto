package com.galvez.projecto.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import com.galvez.projecto.repository.EmployeeRepository;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Data
@Entity
@Table(name = "employees")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Email cannot be empty")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Name is required")
    private String name;

    @NotEmpty(message = "Phone Number is required")
    private String phoneNumber;

    @NotEmpty(message = "Password is required")
    private String password;

     @ManyToOne(fetch = FetchType.EAGER)
     @JoinColumn(name = "projects_id")
     private Project currentProject;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinColumn(name = "employees_id")
    private List<Project> projectList;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "managers_id")
    private Manager manager;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companys_id")
   @JsonBackReference
    private Company company;
    
    private LocalDate employeeDateStarted;

    private String totalRevenue;

    private String totalRevenueAfterTax;


    private String role = "ROLE_EMPLOYEE";

    public List<String> getAuthorities() {
        return Arrays.asList("VIEW_OWN_INFO", "SUBMIT_LEAVE_REQUEST", "VIEW_COMPANY_EVENTS");
    }
}

