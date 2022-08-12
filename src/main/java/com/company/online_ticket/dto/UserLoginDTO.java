package com.company.online_ticket.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {
    @NotEmpty(message = "Email can not be empty")
    private String email;

    @NotEmpty(message = "Password can not be empty")
    @Pattern(regexp = "\\d{4}", message = "password must be 4 numbers")
    private String password;
}
