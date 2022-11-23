package com.example.testSecurity.controller;


import com.example.testSecurity.models.travail;
import com.example.testSecurity.models.user;
import com.example.testSecurity.service.Travailservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/travail")
@CrossOrigin()
public class TravailController {
    @Autowired
    Travailservice travailservice;

    @PostMapping("/add")
    public travail add(@RequestBody travail t){
        return  travailservice.creer(t);
    }

    @GetMapping("/afficher/{id}")
    public travail read(@PathVariable Long id){
        return travailservice.aff(id);
    }


    @PostMapping("update/{id}")
    public  travail update(@RequestBody  travail travail,@PathVariable Long id){
        return  travailservice.modifier(travail,id);
    }

}
