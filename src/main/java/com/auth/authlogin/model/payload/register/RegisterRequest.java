package com.auth.authlogin.model.payload.register;

import com.auth.authlogin.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    private String firstname, lastname, username, email, password;
    private Role role;
}
