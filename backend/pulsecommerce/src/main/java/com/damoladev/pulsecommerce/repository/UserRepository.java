package com.damoladev.pulsecommerce.repository;

import com.damoladev.pulsecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByEmailIgnoreCase(String email);
}
