package com.example.testSecurity.repository;

import com.example.testSecurity.models.user;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Userrepository extends JpaRepository<user, Long> {
    user findByUsername(String username);


}
