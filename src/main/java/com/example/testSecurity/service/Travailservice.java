package com.example.testSecurity.service;

import com.example.testSecurity.models.travail;

public interface Travailservice {
    travail creer(travail travail);
    String supp(Long id);
    travail modifier(travail travail,Long id);
    travail aff(Long id);

}
