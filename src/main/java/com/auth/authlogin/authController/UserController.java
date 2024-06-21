package com.auth.authlogin.authController;

import com.auth.authlogin.Exceptions.PasswordTooShortException;
import com.auth.authlogin.Exceptions.UserAlreadyExistsException;
import com.auth.authlogin.config.JwtService;
import com.auth.authlogin.model.payload.Message.MessageResponse;
import com.auth.authlogin.model.payload.login.LoginRequest;
import com.auth.authlogin.model.payload.register.RegisterRequest;
import com.auth.authlogin.model.payload.register.RegisterResponse;
import com.auth.authlogin.repositories.UserRepository;
import com.auth.authlogin.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
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
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> register(
           @Valid @RequestBody RegisterRequest registerRequest, BindingResult bindingResult
    ){
        if(bindingResult.hasErrors()){
            return ResponseEntity.badRequest().body(bindingResult.getFieldErrors().getFirst().getDefaultMessage());
        }
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
        } catch (PasswordTooShortException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest
    ) {
//        try {
//            User authenticatedUser = authService.login(loginRequest);
//            String jwtToken = jwtService.generateToken(authenticatedUser);
//            LoginRespone response = LoginRespone.builder()
//                    .token(jwtToken)
//                    .expiresIn(jwtService.getExpirationTime())
//                    .build();
//
//            return new ResponseEntity<>(response,HttpStatus.OK);
//
//        } catch (BadCredentialsException | UsernameNotFoundException e ){
//            return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
//        }
        return null;
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtService.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @DeleteMapping("/deleteAllUsers")
    public void deleteAll(){
        userRepository.deleteAll();
    }
}
