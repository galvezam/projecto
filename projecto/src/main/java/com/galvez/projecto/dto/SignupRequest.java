package com.galvez.projecto.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SignupRequest {
    

    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    private String name;
    
    private String phoneNumber;

    private String role;

    private String token; // Add a token field

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
