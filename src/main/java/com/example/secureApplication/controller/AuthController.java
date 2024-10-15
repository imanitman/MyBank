package com.example.secureApplication.controller;

import com.example.secureApplication.constant.ApplicationConstant;
import com.example.secureApplication.model.Customer;
import com.example.secureApplication.model.RequestLoginDto;
import com.example.secureApplication.model.ResponseLoginDto;
import com.example.secureApplication.repository.CustomerRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final Environment env;

    @PostMapping("/register")
    public ResponseEntity<String> reagisterPage (@RequestBody  Customer customer){
        try {
            if (customer != null){
                customer.setPwd(passwordEncoder.encode(customer.getPwd()));
                customer.setCreateDt(new Date(System.currentTimeMillis()));
                customerRepository.save(customer);
                return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
            }
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error occurred here");
        }
    }
    @GetMapping("/user")
    public Customer getUserDetailAfterLogin(Authentication authentication){
        String email = authentication.getName();
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(email);
        return optionalCustomer.orElse(null);
    }

    @PostMapping("/apiLogin")
    public ResponseEntity<ResponseLoginDto> loginPage(@RequestBody RequestLoginDto requestDto){
        Authentication authentication =  UsernamePasswordAuthenticationToken.unauthenticated(requestDto.username(), requestDto.password());
        Authentication authenticationUser = authenticationManager.authenticate(authentication);
        if (authenticationUser.isAuthenticated() && authenticationUser != null){
            String secret = env.getProperty(ApplicationConstant.JWT_SECRET_KEY, ApplicationConstant.JWT_SECRET_DEFAUL);
            if (secret != null){
                SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
                if (secretKey != null){
                    String jwtToken = Jwts.builder().setIssuer("Bank Application").setSubject("JWT Token")
                            .claim("username", authenticationUser.getName())
                            .claim("authorities", authenticationUser .getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                            .setIssuedAt(new Date(System.currentTimeMillis()))
                            .setExpiration(new Date(System.currentTimeMillis() + 30000000))
                            .signWith(secretKey).compact();
                    return ResponseEntity.status(HttpStatus.OK).header(ApplicationConstant.JWT_TOKEN_HEADER,jwtToken)
                            .body(new ResponseLoginDto(HttpStatus.OK.getReasonPhrase(), jwtToken));
            }
        }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseLoginDto(HttpStatus.UNAUTHORIZED.getReasonPhrase(), null));
    }
}

