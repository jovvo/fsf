package com.tlapps.test.fsf.repository;

import com.tlapps.test.fsf.model.UserSession;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserSessionRepository extends BaseRepository<UserSession> {

    @Query(value="select s from UserSession s where s.sessionToken= ?1", nativeQuery = false)
    List<UserSession> findSessionBySessionId(String sessionId);

/*    @Query(value="select s from UserSession s where s.user.email = ?1", nativeQuery = false)
    UserSession findSessionByUser(String email);*/
}
