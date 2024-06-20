package com.auth.authlogin.service;

import com.auth.authlogin.Exceptions.PasswordTooShortException;
import com.auth.authlogin.Exceptions.UserAlreadyExistsException;
import com.auth.authlogin.model.Entity.User;
import com.auth.authlogin.model.payload.login.LoginRequest;
import com.auth.authlogin.model.payload.register.RegisterRequest;
import com.auth.authlogin.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    public void signup(RegisterRequest registerRequest) throws UserAlreadyExistsException {

        User user = User.builder()
                .firstname(registerRequest.getFirstname().trim())
                .lastname(registerRequest.getLastname().trim())
                .username(registerRequest.getUsername().trim())
                .email(registerRequest.getEmail().trim())
                .passwd(encoder.encode(registerRequest.getPassword().trim()))
                .role(registerRequest.getRole())
                .build();

        if(userRepository.existsByUsername(user.getUsername())){
            throw new UserAlreadyExistsException("Username %s already exists".formatted(user.getUsername()));
        }

        if(userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException("email %s already exists".formatted(user.getEmail()));
        }

        if(registerRequest.getPassword().length() < 8){
            throw new PasswordTooShortException("password is too short");
        }

        userRepository.save(user);

    }

    public User login(LoginRequest loginRequest){
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("username %s does not exists".formatted(username))
        );

        if(!encoder.matches(password,user.getPassword())){
            throw new BadCredentialsException("incorrect password for user %s".formatted(username));
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        username,
                        password
                )
        );
       return user;
    }
}
