package com.fyp.alethiaservice.dto.users;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileInfo {

    @JsonProperty("firstname")
    private String firstName;

    @JsonProperty("lastname")
    private String lastName;

    private String email;

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("phone_country_code")
    private String phoneCountryCode;

    @Nullable
    @JsonProperty("phone")
    private String phoneNumber;

    private String gender;

    @JsonProperty("dob")
    private String dateOfBirth;

    @JsonProperty("countryofbirth")
    private String countryOfBirth;

    private String address1;

    private String address2;

    private String city;

    private String county;

    @JsonProperty("country_name")
    private String countryName;

    @JsonProperty("postalcode")
    private String postalCode;

    @JsonProperty("selfie_url")
    private String selfieUrl;

}
