package org.itu.btp.repository;

import org.itu.btp.model.Travaux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravauxRepository extends JpaRepository<Travaux, Integer> {
}
