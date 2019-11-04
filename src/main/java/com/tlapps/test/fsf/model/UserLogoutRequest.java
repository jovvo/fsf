package com.tlapps.test.fsf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@ToString
public final class UserLogoutRequest implements Serializable {
    private String email;
    private String password;
    private String sessionid;
}
