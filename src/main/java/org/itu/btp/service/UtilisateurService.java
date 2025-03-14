package org.itu.btp.service;

import org.itu.btp.model.Utilisateur;
import org.itu.btp.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UtilisateurService {
    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Optional<Utilisateur> findUtilisateurByEmail(String email) {
        return this.utilisateurRepository.findByEmail(email);
    }

    public Optional<Utilisateur> findUtilisateurByContact(String contact) {
        return this.utilisateurRepository.findByContact(contact);
    }

    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        return this.utilisateurRepository.save(utilisateur);
    }
}
