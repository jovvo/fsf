package com.tlapps.test.fsf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@NoArgsConstructor
@ToString
public class UserLoginRequest implements Serializable {
    private String email;
    private String password;


}
