package org.itu.btp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "utilisateur")
public class Utilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_utilisateur")
    private Integer idUtilisateur;

    @Column(name = "nom")
    private String nom;

    @Column(name = "email", unique = true)
    private String email;

    @JsonIgnore
    @Column(name = "mdp")
    private String mdp;

    @Column(name = "contact", unique = true)
    private String contact;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
}
