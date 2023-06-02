package com.jyg.nomina.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@NoArgsConstructor
@Data


public class LoginUser {


    @NotEmpty
    private String userName;
    @NotEmpty
    @Size(min = 8, max = 200)
    private String password;
}
