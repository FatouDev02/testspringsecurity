package com.example.testSecurity.service.impl;

import com.example.testSecurity.models.travail;
import com.example.testSecurity.models.user;
import com.example.testSecurity.repository.TravailRepositir;
import com.example.testSecurity.service.Travailservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Travailimpl implements Travailservice {
    @Autowired
    TravailRepositir travailRepositir;

    @Override
    public travail creer(travail travail) {
        return  travailRepositir.save(travail);
    }

    @Override
    public String supp(Long id) {
                travailRepositir.deleteById(id);
        return"taf supprimé";

    }

    @Override
    public travail modifier(travail travail, Long id) {
        return travailRepositir.findById(id)
                .map(travaillll->{
                    travaillll.setTitre(travail.getTitre());
                    travaillll.setSalaire((travail.getSalaire()));
                    return  travailRepositir.save(travaillll);

                }
                ).orElseThrow(()-> new RuntimeException("ghjklm"));
    }

    @Override
    public travail aff(Long id) {
        return null;
    }
}
