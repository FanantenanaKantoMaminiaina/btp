package org.itu.btp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "maison")
public class Maison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_maison")
    Integer idMaison;

    @Column(name = "description")
    String description;

    @Column(name = "type")
    String type;

    @Column(name = "surface")
    double surface;

    @Column(name = "durre")
    Integer durre;
}
