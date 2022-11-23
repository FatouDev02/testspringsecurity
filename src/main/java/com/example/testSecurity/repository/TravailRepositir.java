package com.example.testSecurity.repository;

import com.example.testSecurity.models.travail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravailRepositir extends JpaRepository<travail,Long> {

}
