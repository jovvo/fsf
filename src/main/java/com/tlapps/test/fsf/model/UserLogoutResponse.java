package com.tlapps.test.fsf.model;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class UserLogoutResponse {
    private UserLogoutResult logoutResult;

    public enum UserLogoutResult {
        SUCCESS,
        FAIL;
    }
}
