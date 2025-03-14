package org.itu.btp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "travaux")
public class Travaux {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_travaux")
    Integer idTravaux;

    @Column(name = "code")
    String code;

    @Column(name = "description")
    String description;

    @Column(name = "prix_unitaire")
    double prixUnitaire;

    @ManyToOne
    @JoinColumn(name = "id_unite")
    Unite unite;
}
