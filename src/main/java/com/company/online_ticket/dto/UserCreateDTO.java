package com.company.online_ticket.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserCreateDTO {

    @NotEmpty(message = "Email can not be empty")
    private String email;

    @NotEmpty(message = "Phone can not be empty")
    @Pattern(regexp = "^9989[012345789][0-9]{7}$", message = "Phone must match pattern!")
    private String phone;

    @NotEmpty(message = "Password can not be empty")
    @Pattern(regexp = "\\d{4}", message = "password must be 4 numbers")
    private String password;

    @NotEmpty(message = "Confirm password can not be empty")
    @Pattern(regexp = "\\d{4}", message = "password must be 4 numbers")
    private String confirmPassword;
}
