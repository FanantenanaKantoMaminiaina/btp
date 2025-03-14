package org.itu.btp.repository;

import org.itu.btp.model.Devis;
import org.itu.btp.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevisRepository extends JpaRepository<Devis, Integer> {
    List<Devis> findByUtilisateur(Utilisateur utilisateur);
}
