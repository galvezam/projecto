package com.galvez.projecto.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
@Table(name = "companys")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Company {
    
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

    private String totalRevenue;

    private String totalProjectCosts;

    private String totalProfit;

    private String totalProfitAfterTax;

    private LocalDate dateStarted;


    @NotEmpty(message = "Password is required")
    private String password;

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinColumn(name = "companys_id")
    private List<Manager> managers = new ArrayList<>();

     @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "company", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinColumn(name = "companys_id")
    private List<Project> projects = new ArrayList<>();
    
    private String role = "ROLE_COMPANY_ADMIN";

    public List<String> getAuthorities() {
        return Arrays.asList("READ_COMPANY_INFO", "UPDATE_COMPANY_INFO", "DELETE_EMPLOYEE", "ADD_MANAGER");
    }
    
}
