package org.itu.btp.repository;

import org.itu.btp.model.Paiement;
import org.itu.btp.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Integer> {

    @Query("SELECT p FROM Paiement p WHERE p.devis.utilisateur = :utilisateur")
    List<Paiement> findByUtilisateur(@Param("utilisateur") Utilisateur utilisateur);

    @Query(value = """
        WITH all_months AS (
           SELECT DISTINCT
               generate_series(
                   DATE_TRUNC('year', MIN(date_paiement)),
                   DATE_TRUNC('month', MAX(date_paiement)),
                   '1 month'::interval
               ) AS mois
           FROM paiement
       )
       SELECT
           TO_CHAR(m.mois, 'YYYY-MM') AS mois_annee,
           COALESCE(SUM(p.montant), 0) AS total_montant
       FROM
           all_months m
       LEFT JOIN
           paiement p ON DATE_TRUNC('month', p.date_paiement) = m.mois
       WHERE
           EXTRACT(YEAR FROM m.mois) = :annee
       GROUP BY
           m.mois
       ORDER BY
           m.mois
    """, nativeQuery = true)
    List<Object[]> getMontantDevisParMois(@Param("annee") Integer annee);


    @Query(value = """
            SELECT
                COUNT(*),
                SUM(montant) AS total_montant,
                SUM(montant_paye) AS total_paye
            FROM
                devis
            """, nativeQuery = true)
    List<Object[]> getMontantTotalDevis();
}
