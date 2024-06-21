package com.auth.authlogin.model.payload.login;

import com.auth.authlogin.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String firstname, lastname, username, email;

    private Role role;
}
