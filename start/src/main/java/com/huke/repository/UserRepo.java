package com.huke.repository;

import com.huke.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author huke
 * @date 2022/08/29/上午11:36
 */
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    Optional<User> findOptionalByUsername(String username);


    long countByEmail(String email);

    long countByUsername(String mobile);

    long countByMobile(String mobile);
}
