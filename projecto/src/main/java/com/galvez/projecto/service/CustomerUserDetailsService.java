package com.galvez.projecto.service;

import com.galvez.projecto.model.Employee;

import java.util.Arrays;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.galvez.projecto.repository.EmployeeRepository;
import com.galvez.projecto.repository.ManagerRepository;
import com.galvez.projecto.service.CustomerUserDetails;
import com.galvez.projecto.utils.JWTUtils;

@Service
public class CustomerUserDetailsService implements UserDetailsService{

//    @Autowired
//    private ManagerRepository managerRepository;

    // @Autowired
    // private EmployeeRepository employeeRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return jwtUtils.getUserDetailsFromToken(username);
        // Employee employee = employeeRepository.findByEmail(username)
            // .orElseThrow(() -> new UsernameNotFoundException("Username/Email not found"));

        // You can customize the authorities based on your application's logic

        // Collection<? extends GrantedAuthority> authorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));

        // return new CustomerUserDetails(employee.getEmail(), employee.getPassword(), authorities);

        // return (UserDetails) employeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username/Email not found"));
    }

    // @Override
    // public Employee loadUserByUsername(String username) throws UsernameNotFoundException {
    //     return employeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username/Email not found"));
    // }

    // @Override
    // public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    //     return managerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Username/Email not found"));
    // }

}
