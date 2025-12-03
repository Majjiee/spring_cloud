package com.example.service_voiture.services;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.service_voiture.entities.Client;

@FeignClient(name="SERVICE-CLIENT")
public interface ClientService{
    @GetMapping(path="/clients/{id}")
    Client clientById(@PathVariable Long id);
}