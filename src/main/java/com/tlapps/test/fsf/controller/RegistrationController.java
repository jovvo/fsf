package com.tlapps.test.fsf.controller;


import com.tlapps.test.fsf.model.RegistrationRequest;
import com.tlapps.test.fsf.model.RegistrationResponse;
import com.tlapps.test.fsf.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@CrossOrigin
@RequestMapping("/fsf")
@RestController
public final class RegistrationController {

    private RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<RegistrationResponse> requestReport(
            @RequestBody RegistrationRequest registrationRequest) {

            registrationService.registerUser(registrationRequest);

            RegistrationResponse response = new RegistrationResponse();
            response.setMessage("user_registration_success");
            return new ResponseEntity<RegistrationResponse>(response, HttpStatus.CREATED);
        }



}