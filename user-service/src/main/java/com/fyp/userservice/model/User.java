package com.fyp.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    // id missing

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

    @Column(name = "email", length = 120, nullable = false, unique = true)
    private String email;

    @Column(name = "phone_country_code", length = 5, nullable = false)
    private String phoneCountryCode;

    @Column(name = "phone_number", length = 20, nullable = false, unique = true)
    private String phoneNumber;

//    private String gender; // I think there is something going on with gender --> Needs to be investigated

    @Column(name = "date_of_birth", length = 15)
    private String dateOfBirth;

    @Column(name = "country_of_birth", length = 50)
    private String countryOfBirth;

    @Column(name = "address_1", length = 50)
    private String address1;

    @Column(name = "address_2", length = 50)
    private String address2;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "county", length = 20)
    private String county;

    @Column(name = "country_name", length = 20)
    private String countryName;

    @Column(name = "postal_code", length = 15)
    private String postalCode;
}
