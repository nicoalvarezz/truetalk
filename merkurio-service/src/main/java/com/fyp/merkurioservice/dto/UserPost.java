package com.fyp.merkurioservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserPost {

    @NotNull
    @NotBlank
    @Length(min = 1, max = 250)
    private String text;

    @NotNull
    @NotBlank
    private String user;
}
