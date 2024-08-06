package com.galvez.projecto.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.galvez.projecto.model.Company;
import com.galvez.projecto.model.Employee;
import com.galvez.projecto.model.Manager;
import com.galvez.projecto.repository.CompanyRepository;
import com.galvez.projecto.repository.EmployeeRepository;
import com.galvez.projecto.repository.ManagerRepository;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

@Service
public class JWTUtils {

    private static final long EXPIRATION_TIME = 1000 * 60 * 24 * 7; // 1 week
    private static final long CLOCK_SKEW = 300000; // 5 minutes in milliseconds
    private final SecretKey key;


//    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long validityInMilliseconds = 3600000;

    public JWTUtils() {
        String secreteString = "843567893696976453275974432697R634976R738467TR678T34865R6834R8763T478378637664538745673865783678548735687R3";
        byte[] keyBytes = Base64.getDecoder().decode(secreteString.getBytes(StandardCharsets.UTF_8));
        this.key = new SecretKeySpec(keyBytes, "HmacSHA256");
    }


    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;
    
    // public String generateToken(UserDetails userDetails) {
    //     return Jwts.builder()
    //             .setSubject(userDetails.getUsername())
    //             .setIssuedAt(new Date(System.currentTimeMillis()))
    //             .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
    //             .signWith(key)
    //             .compact();
    // }

    // public String generateToken(UserDetails userDetails) {
    //     Date now = new Date();
    //     Date expiryDate = new Date(now.getTime() + EXPIRATION_TIME);
        
    //     return Jwts.builder()
    //             .setSubject(userDetails.getUsername())
    //             .claim("authorities", userDetails.getAuthorities())
    //             .setIssuedAt(now)
    //             .setExpiration(expiryDate)
    //             .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS512)
    //             .compact();
    // }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername());
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser()
                .setSigningKey(key)
                .setAllowedClockSkewSeconds(CLOCK_SKEW / 1000) // Allow 5 minutes clock skew
                .build()
                .parseClaimsJws(token)
                .getBody());
    }
    
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        return Jwts.parser().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = extractExpiration(token);
        return expiration.before(new Date());
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public UserDetails getUserDetailsFromToken(String token) {
        String username = extractUsername(token);
        
        // Implement logic to fetch user details based on username and role
        if (username.equals("admin")) {
            return createAdminUserDetails(username);
        } 
        else if (username.equals("company_admin")) {
            return createCompanyUserDetails(username);
        }
        else if (username.equals("manager")) {
            return createManagerUserDetails(username);
        } else if (username.equals("employee")) {
            return createEmployeeUserDetails(username);
        } else {
            // Handle other user cases or fetch user details from a database
            return null; // Return null if user not found
        }
    }

    public UserDetails createAdminUserDetails(String username) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
            }

            @Override
            public String getPassword() {
                // String user = this.getUsername();
                return getCompanyPasswordFromDatabase(username);
            }
    
            @Override
            public String getUsername() {
                return username;
            }
    
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }
    
            @Override
            public boolean isAccountNonLocked() {
                return true;
            }
    
            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }
    
            @Override
            public boolean isEnabled() {
                return true;
            }
    
            // Implement other UserDetails methods for admin
        };
    }

    public UserDetails createCompanyUserDetails(String username) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Arrays.asList(
                    // new SimpleGrantedAuthority("ROLE_COMPANY"),
                    new SimpleGrantedAuthority("ROLE_EMPLOYEE"),
                    new SimpleGrantedAuthority("ROLE_MANAGER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"),
                    new SimpleGrantedAuthority("ROLE_COMPANY_ADMIN")
                );
            }
    
            @Override
            public String getPassword() {
                // String user = this.getUsername();
                return getCompanyPasswordFromDatabase(username);
            }
    
            @Override
            public String getUsername() {
                return username;
            }
    
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }
    
            @Override
            public boolean isAccountNonLocked() {
                return true;
            }
    
            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }
    
            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
    
    public UserDetails createManagerUserDetails(String username) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_MANAGER"));
            }
    
            // Implement other UserDetails methods for manager


            @Override
            public String getPassword() {
                // String user = this.getUsername();
                return getManagerPasswordFromDatabase(username);
            }
    
            @Override
            public String getUsername() {
                return username;
            }
    
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }
    
            @Override
            public boolean isAccountNonLocked() {
                return true;
            }
    
            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }
    
            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    };

    
    
    
    public UserDetails createEmployeeUserDetails(String username) {
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Collections.singletonList(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
            }
    
            // Implement other UserDetails methods for employee

            @Override
            public String getPassword() {
                // String user = this.getUsername();
                return getEmployeePasswordFromDatabase(username);
            }
    
            @Override
            public String getUsername() {
                return username;
            }
    
            @Override
            public boolean isAccountNonExpired() {
                return true;
            }
    
            @Override
            public boolean isAccountNonLocked() {
                return true;
            }
    
            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }
    
            @Override
            public boolean isEnabled() {
                return true;
            }
        
        };
    }

    private String getCompanyPasswordFromDatabase(String username) {
        // Implement logic to fetch password from a secure source (e.g., database)
        // Example: return password from a database query

        // return companyRepository.getPasswordForUser("company_user");
        Company company = companyRepository.findByEmail(username).orElse(null);
        return company.getPassword();
    }

    private String getEmployeePasswordFromDatabase(String username) {
        // Implement logic to fetch password from a secure source (e.g., database)
        // Example: return password from a database query

        // return companyRepository.getPasswordForUser("company_user");
        Employee employee = employeeRepository.findByEmail(username).orElse(null);
        return employee.getPassword();
    }
    private String getManagerPasswordFromDatabase(String username) {
        // Implement logic to fetch password from a secure source (e.g., database)
        // Example: return password from a database query

        // return companyRepository.getPasswordForUser("company_user");
        Manager manager = managerRepository.findByEmail(username).orElse(null);
        return manager.getPassword();
    }

    // private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
    //     return claimsTFunction.apply(Jwts.parser()
    //             .setSigningKey(key)
    //             .setAllowedClockSkewSeconds(CLOCK_SKEW / 1000) // Allow 5 minutes clock skew
    //             .build()
    //             .parseClaimsJws(token)
    //             .getBody());
    // }

    // public boolean isValidToken(String token, UserDetails userDetails) {
    //     final String username = extractUsername(token);
    //     return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    // }

    // private boolean isTokenExpired(String token) {
    //     return extractClaims(token, Claims::getExpiration).before(new Date());
    // }
}