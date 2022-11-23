package com.example.testSecurity.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.testSecurity.models.Role;
import com.example.testSecurity.models.user;
import com.example.testSecurity.service.userservice;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin()
@Controller
@RequestMapping("/api")
public class usercontroller {
    @Autowired
    userservice  userservice;

    @PostMapping("/adduser")
    public ResponseEntity<user> adduser(@RequestBody user us){
        URI uri=URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/adduser").toUriString());
        //return ResponseEntity.ok().body( userservice.ajouter(us));
        return ResponseEntity.created(uri).body( userservice.ajouter(us));

    }

    @PostMapping("/addrole")
    public ResponseEntity<Role> addRole(@RequestBody Role role ){
        return ResponseEntity.ok().body( userservice.ajoutrole(role));
    }
    @PostMapping("/addroletoUser")
    public ResponseEntity<?> addroletouser(@RequestBody RoletoUser roletoUser ){
        URI uri=URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/addroletoUser").toUriString());
        //return ResponseEntity.ok().body( userservice.ajouter(us));
        userservice.addroletoUser(roletoUser.getUsername(),roletoUser.getRolename());
        return ResponseEntity.ok().build();

    }
    @GetMapping("/refreshtoken")
    public void refreshtoken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("b ")){

            try {
                String refresh_token= authorizationHeader.substring("b ".length());
                Algorithm algorithm=Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT=verifier.verify(refresh_token);
                //gestion des authorizations
                String username=decodedJWT.getSubject();
                user user=userservice.getuser(username);


                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10*60*1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles",user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                Map<String,String> tokens=new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            } catch (Exception e){
                //log.error("error loggingg",e.getMessage());
                response.setHeader("error",e.getMessage());
                response.setStatus(FORBIDDEN.value());

                //response.sendError(FORBIDDEN.value());
                Map<String,String> error=new HashMap<>();
                error.put("error_message",e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        } else {
            throw new RuntimeException("Refresh token is missing");
        }

    }


    @GetMapping("/all")
    public ResponseEntity<List<user>>getUsers(){

        return  ResponseEntity.ok().body(userservice.lister());
    }
    @GetMapping("/afficher/{id}")
    public user read(@PathVariable Long id){
        return userservice.afficher(id);
    }



    @GetMapping("/lister")
    public List<user> list(){
        return  userservice.lister();
    }
    @PostMapping("update/{id}")
    public  user update(@RequestBody  user user,@PathVariable Long id){
        return  userservice.modifier(user,id);
    }
}
@Data
class RoletoUser{
    private String username;
    private String rolename;
}
