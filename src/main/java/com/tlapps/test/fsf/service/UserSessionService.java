package com.tlapps.test.fsf.service;

import com.tlapps.test.fsf.model.User;
import com.tlapps.test.fsf.model.UserSession;
import com.tlapps.test.fsf.repository.UserRepository;
import com.tlapps.test.fsf.repository.UserSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@Transactional
public class UserSessionService {
    private final UserSessionRepository userSessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public UserSessionService(UserSessionRepository userSessionRepository,
                              UserRepository userRepository) {
        this.userSessionRepository = userSessionRepository;
        this.userRepository = userRepository;
    }

    public String createSession(User user) {
    	UserSession userSession = null; //userSessionRepository.findSessionByUser(user.getEmail());
    	if(userSession != null) {
    		userSession.setChangedOn(LocalDateTime.now());
    		userSessionRepository.save(userSession);
    		return userSession.getSessionToken();
    	}
    	
        String sessionId = UUID.randomUUID().toString();
        UserSession newUserSession = new UserSession();
        newUserSession.setSessionToken(sessionId);
        newUserSession.setAttributes("");
        //newUserSession.setUser(user);
        newUserSession.setChangedOn(LocalDateTime.now());

        userSessionRepository.save(newUserSession);
        userSessionRepository.flush();

        return sessionId;
    }

    public Optional<UserSession> findUserSession(String sessionId) {
        Optional<UserSession> userSession =
                userSessionRepository.findSessionBySessionId(sessionId)
                        .stream().filter(Objects::nonNull).findFirst();

        return userSession;
    }

}
