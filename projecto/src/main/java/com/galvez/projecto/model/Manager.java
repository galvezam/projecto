package com.galvez.projecto.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import org.springframework.security.core.userdetails.UserDetails;

// IN FUTURE IMPLEMENT THIS
// public class Manager implements UserDetails {

@Data
@Entity
@Table(name = "managers")
public class Manager {
    
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

//    private String role;

    private LocalDate managerDateStarted;

    private String totalRevenue;

    private String totalRevenueAfterTax;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinColumn(name = "managers_id")
    private List<Employee> employees = new ArrayList<>();

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    // @JoinColumn(name = "managers_id")
    private List<Project> projects = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "companys_id")
    private Company company;

    
    private String role = "ROLE_MANAGER";

    public List<String> getAuthorities() {
        return Arrays.asList("VIEW_EMPLOYEES", "ASSIGN_TASKS", "APPROVE_LEAVE_REQUESTS");
    }

//        /* required methods for spring security */
//        @Override
//        public Collection<? extends GrantedAuthority> getAuthorities() {
//            return List.of(new SimpleGrantedAuthority(role));
//        }
//
//        @Override
//        public String getUsername() {
//            return email;
//        }
//
//        @Override
//        public boolean isAccountNonExpired() {
//            return true;
//            /*return UserDetails.super.isAccountNonExpired();*/
//        }
//
//        @Override
//        public boolean isAccountNonLocked() {
//            return true;
//            /*return UserDetails.super.isAccountNonLocked();*/
//        }
//
//        @Override
//        public boolean isCredentialsNonExpired() {
//            return true; /*UserDetails.super.isCredentialsNonExpired()*/
//        }
//
//        @Override
//        public boolean isEnabled() {
//            return true; /*UserDetails.super.isEnabled()*/
//        }
}
