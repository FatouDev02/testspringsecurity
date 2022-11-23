package com.example.testSecurity.repository;

import com.example.testSecurity.models.Role;
import com.example.testSecurity.models.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Rolerepository extends JpaRepository<Role,Long> {
    Role findByName(String name);

}
