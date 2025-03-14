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
@Table(name = "maison_travaux")
public class MaisonTravaux {
    @EmbeddedId
    IdMaisonTravaux idMaisonTravaux;

    @ManyToOne
    @MapsId("idMaison")
    @JoinColumn(name = "id_maison")
    Maison maison;

    @ManyToOne
    @MapsId("idTravaux")
    @JoinColumn(name = "id_travaux")
    Travaux travaux;

    @Column(name = "quantite")
    double quantite;

    @Column(name = "total")
    double total;
}
