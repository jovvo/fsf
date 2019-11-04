package com.tlapps.test.fsf.service;


import com.tlapps.test.fsf.model.RegistrationRequest;
import com.tlapps.test.fsf.model.User;
import com.tlapps.test.fsf.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@Data
public final class RegistrationService {

    private UserRepository userRepository;

    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(RegistrationRequest registrationRequest) {
        User user = new User();
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(registrationRequest.getPassword());
        user.setChangedOn(LocalDateTime.now());

        userRepository.save(user);
    }

}