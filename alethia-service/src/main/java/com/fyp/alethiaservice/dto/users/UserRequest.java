package com.fyp.alethiaservice.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {

    @Email
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @JsonProperty("phone_number")
    private String phoneNumber;
}
