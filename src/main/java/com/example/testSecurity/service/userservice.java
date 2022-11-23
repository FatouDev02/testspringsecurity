package com.example.testSecurity.service;

import com.example.testSecurity.models.Role;
import org.springframework.stereotype.Service;
import com.example.testSecurity.models.user;

import java.security.SecureRandom;
import java.util.List;

@Service
public interface userservice {
    user ajouter(user u);
    String supp(Long id);
    user afficher(Long id);
    user modifier(user u,Long id);
    Role ajoutrole(Role role);
    user getuser(String username);
    List<user> lister();

    void addroletoUser(String username,String roleName);

}
