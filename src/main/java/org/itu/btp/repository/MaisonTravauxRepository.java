package org.itu.btp.repository;

import org.itu.btp.model.Maison;
import org.itu.btp.model.MaisonTravaux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaisonTravauxRepository extends JpaRepository<MaisonTravaux, Integer> {
    List<MaisonTravaux> findByMaison(Maison maison);
}
