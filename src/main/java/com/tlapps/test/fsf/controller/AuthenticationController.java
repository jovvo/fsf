package com.tlapps.test.fsf.controller;


import com.tlapps.test.fsf.model.UserLoginRequest;
import com.tlapps.test.fsf.model.UserLoginResponse;
import com.tlapps.test.fsf.service.UserAuthenticationService;
import com.tlapps.test.fsf.service.UserSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@CrossOrigin
@RequestMapping("/fsf")
@RestController
public final class AuthenticationController {
    private final UserAuthenticationService userAuthenticationService;
    private final UserSessionService userSessionService;

    @Autowired
    public AuthenticationController(
            UserAuthenticationService userAuthenticationService,
            UserSessionService userSessionService) {
        this.userAuthenticationService = userAuthenticationService;
        this.userSessionService = userSessionService;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<UserLoginResponse> loginUser(
            @RequestBody UserLoginRequest userLoginRequest
    ) {
        Optional<UserLoginResponse> response = Optional.empty();

        String userSessionId = userAuthenticationService.checkUserPassword(
                userLoginRequest.getEmail(), userLoginRequest.getPassword());

        UserLoginResponse userLoginResponse = new UserLoginResponse();

        userLoginResponse.setUserSessionId(userSessionId);
        if (userSessionId.compareTo("") != 0) {
            userLoginResponse.setLoginResult(UserLoginResponse.UserLoginResult.SUCCESS);
        } else {
            userLoginResponse.setLoginResult(UserLoginResponse.UserLoginResult.FAIL);
        }
        response = Optional.of(userLoginResponse);

        return new ResponseEntity<UserLoginResponse>(response.get(), HttpStatus.ACCEPTED);
    }


}
