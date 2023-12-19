package com.glady.challenge.repository;

import com.glady.challenge.model.user.GladyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GladyUserRepository extends JpaRepository<GladyUser, Long> {

    Optional<GladyUser> findByUsernameIgnoreCase(String username);
}
