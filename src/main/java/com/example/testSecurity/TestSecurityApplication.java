package com.example.testSecurity;

import com.example.testSecurity.models.Role;
import com.example.testSecurity.models.user;
import com.example.testSecurity.service.userservice;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class TestSecurityApplication {

	public static void main(String[] args) {

		SpringApplication.run(TestSecurityApplication.class, args);
	}


	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(userservice uservice){
		return args -> {
			//crée des roles des linitialisation de lapp
			uservice.ajoutrole(new Role(null,"Role_USER"));
			uservice.ajoutrole(new Role(null,"Role_ADMIN"));
			uservice.ajoutrole(new Role(null,"Role_SuperAdmin"));

			//crée des roles des linitialisation de lapp
			uservice.ajouter(new user(null,"coul","fatou","fcusername","fc@gmail.com","fcpassword",new ArrayList<>()));
			uservice.ajouter(new user(null,"coul","lyd","lydusername","lyd@gmail.com","lydpassword",new ArrayList<>()));


			//attribuer un role a un user
			uservice.addroletoUser("fcusername","Role_SuperAdmin");
			uservice.addroletoUser("lydusername","Role_USER");

		};

	}
}
