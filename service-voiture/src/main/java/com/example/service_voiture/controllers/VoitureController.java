package com.example.service_voiture.controllers;

import com.example.service_voiture.entities.Client;
import com.example.service_voiture.entities.Voiture;
import com.example.service_voiture.repositories.VoitureRepository;
import com.example.service_voiture.services.ClientService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/voitures")
public class VoitureController {

    @Autowired
    private VoitureRepository voitureRepository;

    @Autowired
    private ClientService clientService;

    // -----------------------
    // GET ALL VOITURES
    // -----------------------
    @GetMapping
    public ResponseEntity<?> findAll() {
        List<Voiture> voitures = voitureRepository.findAll();
        return ResponseEntity.ok(voitures);
    }

    // -----------------------
    // GET VOITURE BY ID
    // -----------------------
    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        Optional<Voiture> optionalVoiture = voitureRepository.findById(id);

        if (optionalVoiture.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }

        Voiture voiture = optionalVoiture.get();
        Client client = clientService.clientById(voiture.getId_client());
        voiture.setClient(client);

        return ResponseEntity.ok(voiture);
    }

    // -----------------------
    // GET VOITURES BY CLIENT ID
    // -----------------------
    @GetMapping("/client/{clientId}")
    public ResponseEntity<?> findByClient(@PathVariable Long clientId) {
        try {
            Client client = clientService.clientById(clientId);

            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + clientId);
            }

            List<Voiture> voitures = voitureRepository.findAll()
                    .stream()
                    .filter(v -> v.getId_client().equals(clientId))
                    .toList();

            return ResponseEntity.ok(voitures);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    // -----------------------
    // CREATE VOITURE FOR CLIENT
    // -----------------------
    @PostMapping("/{clientId}")
    public ResponseEntity<?> save(@PathVariable Long clientId, @RequestBody Voiture voiture) {
        try {
            Client client = clientService.clientById(clientId);

            if (client == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Client not found with ID: " + clientId);
            }

            voiture.setId_client(clientId);
            voiture.setClient(client);

            Voiture saved = voitureRepository.save(voiture);
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving voiture: " + e.getMessage());
        }
    }

    // -----------------------
    // UPDATE VOITURE
    // -----------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Voiture updated) {

        Optional<Voiture> optionalVoiture = voitureRepository.findById(id);

        if (optionalVoiture.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Voiture not found with ID: " + id);
        }

        Voiture voiture = optionalVoiture.get();

        if (updated.getMatricule() != null) {
            voiture.setMatricule(updated.getMatricule());
        }
        if (updated.getMarque() != null) {
            voiture.setMarque(updated.getMarque());
        }
        if (updated.getModel() != null) {
            voiture.setModel(updated.getModel());
        }

        Voiture saved = voitureRepository.save(voiture);
        return ResponseEntity.ok(saved);
    }
}
