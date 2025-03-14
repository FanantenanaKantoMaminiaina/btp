package org.itu.btp.repository;

import org.itu.btp.model.TypeFinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeFinitionRepository extends JpaRepository<TypeFinition, Integer> {

}
