package com.tlapps.test.fsf.repository;

import com.tlapps.test.fsf.model.User;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends BaseRepository<User> {

    @Query(value="select us from User us where us.email = ?1", nativeQuery = false)
    User findByEmail(String username);

}
