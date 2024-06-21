package com.auth.authlogin.model.payload.register;

import com.fasterxml.jackson.core.JacksonException;
import com.auth.authlogin.model.Role;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.IOException;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {


    private String firstname;

    private String lastname;

    @Size(min = 5, max = 20)
    @NotBlank(message = "username is mandatory field")
    private String username;

    @Email
    @NotBlank(message = "email is mandatory field")
    private String email;

    @NotBlank(message = "password is mandatory field")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=]).{8,}$",
            message = "password must contain at least 1 uppercase, 1 lowercase, 1 special character, 1 digit, min length must be 8 characters")
    private String password;


    @JsonDeserialize(using = RoleDeserializer.class)
    private Role role;


    static class RoleDeserializer extends JsonDeserializer<Role> {

        @Override
        public Role deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
            String value = jsonParser.getText().toUpperCase();
            if (!value.startsWith("ROLE_")) {
                value = "ROLE_" + value;
            }
            return Role.valueOf(value);
        }
    }


}

