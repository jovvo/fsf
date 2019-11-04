package com.tlapps.test.fsf.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@Data
@NoArgsConstructor
@ToString
public final class UserLoginResponse implements Serializable {
    private UserLoginResult loginResult;
    private String userSessionId;

    public enum UserLoginResult {
        SUCCESS,
        FAIL;
    }
}
