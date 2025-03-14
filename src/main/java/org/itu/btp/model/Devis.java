package org.itu.btp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "devis")
public class Devis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_devis")
    Integer idDevis;

    @Column(name = "date_devis")
    Date dateDevis;

    @Column(name = "debut_travaux")
    Date debutTravaux;

    @Column(name = "fin_travaux")
    Date finTravaux;

    @Column(name = "lieu")
    String lieu;

    @Column(name = "reference")
    String reference;

    @Column(name = "montant")
    double montant;

    @Column(name = "montant_paye")
    double montantPaye;

    double paiementEffectue;

    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "id_type")
    TypeFinition typeFinition;

    @ManyToOne
    @JoinColumn(name = "id_maison")
    Maison maison;
}
