package org.itu.btp.repository;

import org.itu.btp.model.Maison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaisonRepository extends JpaRepository<Maison, Integer> {
}
