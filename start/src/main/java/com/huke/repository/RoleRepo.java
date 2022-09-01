package com.huke.repository;

import com.huke.domain.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author huke
 * @date 2022/08/29/上午11:39
 */
@Repository
public interface RoleRepo extends JpaRepository<Role,Long> {

    Optional<Role> findOptionalByAuthority(String authority);
}
