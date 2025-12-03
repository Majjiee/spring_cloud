package com.example.service_voiture.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.service_voiture.entities.Voiture;

@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> { }
