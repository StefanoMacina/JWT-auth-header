package com.auth.authlogin.authController;

import com.auth.authlogin.Exceptions.UserAlreadyExistsException;
import com.auth.authlogin.config.JwtService;
import com.auth.authlogin.model.Entity.User;
import com.auth.authlogin.model.payload.login.LoginRequest;
import com.auth.authlogin.model.payload.login.LoginRespone;
import com.auth.authlogin.model.payload.register.RegisterRequest;
import com.auth.authlogin.model.payload.register.RegisterResponse;
import com.auth.authlogin.repositories.UserRepository;
import com.auth.authlogin.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest registerRequest
    ){
        try{
            authService.signup(registerRequest);
            RegisterResponse resp = RegisterResponse.builder()
                    .firstname(registerRequest.getFirstname())
                    .lastname(registerRequest.getLastname())
                    .email(registerRequest.getEmail())
                    .username(registerRequest.getUsername())
                    .role(registerRequest.getRole())
                    .message("user registered successfully!")
                    .build();
            return new ResponseEntity<>(resp,HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest
    ) {
        try {
            User authenticatedUser = authService.login(loginRequest);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginRespone response = LoginRespone.builder()
                    .token(jwtToken)
                    .expiresIn(jwtService.getExpirationTime())
                    .build();

            return new ResponseEntity<>(response,HttpStatus.OK);

        } catch (BadCredentialsException | UsernameNotFoundException e ){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteAllUsers")
    public void deleteAll(){
        userRepository.deleteAll();
    }
}
