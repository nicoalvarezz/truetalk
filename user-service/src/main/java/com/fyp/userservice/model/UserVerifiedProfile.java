package com.fyp.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "user_verified_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserVerifiedProfile {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @OneToOne
    @MapsId
    @JoinColumn(name = "uuid")
    private User user;

    @Column(name = "first_name", length = 50, nullable = false)
    private String firstName;

    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;

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
