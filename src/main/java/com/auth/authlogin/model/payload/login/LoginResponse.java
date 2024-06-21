package com.auth.authlogin.model.payload.login;

import com.auth.authlogin.model.Entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseCookie;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private ResponseCookie token;
    private UserDto userDto;
}
