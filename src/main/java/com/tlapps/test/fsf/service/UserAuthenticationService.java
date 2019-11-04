package com.tlapps.test.fsf.service;

import com.tlapps.test.fsf.model.User;
import com.tlapps.test.fsf.model.UserSession;
import com.tlapps.test.fsf.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UserAuthenticationService {
    private final UserRepository userRepository;
    private final UserSessionService userSessionService;

    @Autowired
    public UserAuthenticationService(
            UserRepository userRepository,
            UserSessionService userSessionService) {
        this.userRepository = userRepository;
        this.userSessionService = userSessionService;
    }

    public String checkUserPassword(String email, String userPassword) {
        String sessionId = "";
        User user = userRepository.findByEmail(email);

        if (user != null) {

            String savedUserPassword = user.getPassword();

            if (savedUserPassword.equals(userPassword)) {

                UserSession userSession = null; // user.get().getUserSession();
                if (userSession == null) {
                    sessionId = userSessionService.createSession(user);
                } else {
                    sessionId = userSession.getSessionToken();
                }
            }
        }

        return sessionId;
    }
}
