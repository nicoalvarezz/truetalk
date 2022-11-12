package com.fyp.userservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    @NotNull
    @NotBlank
    @JsonProperty("firstname")
    private String firstName;

    @NotNull
    @NotBlank
    @JsonProperty("lastname")
    private String lastName;

    @Email
    @NotNull
    @NotBlank
    private String email;

    // TODO;
    // Not sure what was this ----> Needs to be investigated
    @JsonProperty("account_id")
    private String accountId;

    @NotNull
    @NotBlank
    @JsonProperty("phone_country_code")
    private String phoneCountryCode;

    @NotNull
    @NotBlank
    @Pattern(regexp = "^[+]*[(]?[0-9]{1,4}[)]?[-\\s\\./0-9]*$")
    @JsonProperty("phone")
    private String phoneNumber;

    @NotNull
    @NotNull
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    @JsonProperty("dob")
    private String dateOfBirth;

    @NotNull
    @NotBlank
    @JsonProperty("countryofbirth")
    private String countryOfBirth;

    @NotNull
    @NotBlank
    private String address1;

    @Nullable
    private String address2;

    @NotNull
    @NotBlank
    private String city;

    @NotNull
    @NotBlank
    private String county;

    @NotNull
    @NotBlank
    @JsonProperty("country_name")
    private String countryName;

    @NotNull
    @NotBlank
    @JsonProperty("postalcode")
    private String postalCode;
}
