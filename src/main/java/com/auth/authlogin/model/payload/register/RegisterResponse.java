package com.auth.authlogin.model.payload.register;

import com.auth.authlogin.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {
    private String firstname, lastname, username, email, message;
    private Role role;
}
