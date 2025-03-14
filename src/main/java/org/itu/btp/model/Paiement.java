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
@Table(name = "paiement")
public class Paiement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paiement")
    Integer idPaiement;

    @Column(name = "reference")
    String reference;

    @Column(name = "montant")
    double montant;

    @Column(name = "date_paiement")
    Date datePaiement;

    @ManyToOne
    @JoinColumn(name = "id_devis")
    Devis devis;
}
