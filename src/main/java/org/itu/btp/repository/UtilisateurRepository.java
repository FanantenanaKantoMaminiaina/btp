package org.itu.btp.repository;

import org.itu.btp.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    public Optional<Utilisateur> findByEmail(String email);
    public Optional<Utilisateur> findByContact(String contact);
}
