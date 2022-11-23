package com.example.testSecurity.service.impl;


import com.example.testSecurity.models.Role;
import com.example.testSecurity.models.user;
import com.example.testSecurity.repository.Rolerepository;
import com.example.testSecurity.repository.Userrepository;
import com.example.testSecurity.service.userservice;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
//
// @AllArgsConstructor
@RequiredArgsConstructor
@Transactional
@Slf4j
public class userserviceimpl implements userservice, UserDetailsService {
    @Autowired
    Userrepository userrepository;
    @Autowired
    Rolerepository rolerepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        user user=userrepository.findByUsername(username);
        if(user == null){
            log.error("Utilisateur non trouvé");
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        } else{
            log.info("Utilisateur  trouvé",username);
        }
        //noonnnnnnnnnnnn compris
        Collection<SimpleGrantedAuthority> authorities= new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName())));
        return new  org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),authorities);
        ////////////////////////
    }
    @Override
    public user ajouter(user u) {
        //comment
        log.info("saving new user {} to database",u.getUsername());
        //a l'enregistrement on recupère le passwor et le l'encode
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        return userrepository.save(u);
    }
    @Override
    public Role ajoutrole(Role role) {
        log.info("saving role user {} to database",role.getName());
        return rolerepository.save(role);
    }

    @Override
    public String supp(Long id) {
        this.userrepository.deleteById(id);
        return "utilisateur supprimé";
    }

    @Override
    public List<user> lister() {
        log.info("fecthing all users {} ");

        return userrepository.findAll();
    }

    @Override
    public void addroletoUser(String username, String roleName) {
        log.info("ADDing role {} to user {}",roleName,username);
        user u=userrepository.findByUsername(username);
        Role role=rolerepository.findByName(roleName);
        //will executing because we have transactionnal
        u.getRoles().add(role);

    }

    @Override
    public user afficher(Long id) {
        return userrepository.findById(id).get();
    }

    @Override
    public user modifier(user u,Long id) {
        return userrepository.findById(id)
                .map(us2 ->{
                    us2.setNom(u.getNom());
                    us2.setPrenom(u.getPrenom());
                    return userrepository.save(us2);
                        }).orElseThrow(()->new RuntimeException("cet utilisateur n'existepas!!"));
    }



    @Override
    public user getuser(String username) {
        log.info("fecthing user {} ",username);

        return userrepository.findByUsername(username);

    }


}
