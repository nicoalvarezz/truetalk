package com.fyp.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserRequest {

    @Email
    @NotNull
    @NotBlank
    private String email;

    @Nullable
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotNull
    @NotBlank
    // Minimum eight characters, at least one uppercase letter, one lowercase letter, one number and one special character
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,45}$")
    private String password;
}
