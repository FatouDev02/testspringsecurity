package com.example.testSecurity.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
@Data
@Entity
public class travail {
    @Id
    private Long id;
    private String titre;
    private String salaire;
}
