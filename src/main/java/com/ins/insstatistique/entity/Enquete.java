package com.ins.insstatistique.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "enquetes") // Optional: specify table name
@Data // Lombok annotation for boilerplate code
@NoArgsConstructor // JPA requires a no-arg constructor
@AllArgsConstructor // Optional: useful constructor
public class Enquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id; // Primary key

    @Column(name = "identifiant_stat") // Map to snake_case if desired
    private String identifiantStat;

    @Column(name = "nom_sociale")
    private String nomSociale;

    private String adresse;
    private String telephone;
    private String fax;

    @Column(name = "nom_repondant")
    private String nomRepondant;

    @Column(name = "mail_repondant")
    private String mailRepondant;

    @Column(name = "branche_activite")
    private String brancheActivite;

    private Boolean exportatrice; // Mapped from "true"/"false"

    @Column(name = "variaison_saisoniere")
    private Boolean variaisonSaisoniere; // Mapped from "true"/"false"

    @Column(name = "situation_1er_trimestre")
    private String situation1erTrimestre; // e.g., "bonne", "moyenne"

    @Column(name = "situation_2eme_trimestre")
    private String situation2emeTrimestre;

    @Column(name = "situation_future")
    private String situationFuture; // e.g., "meme"

    @Column(name = "effectifs_1er_trimestre")
    private String effectifs1erTrimestre; // e.g., "same", "up", "down"

    @Column(name = "effectifs_2eme_trimestre")
    private String effectifs2emeTrimestre;

    @Column(name = "effectifs_futur")
    private String effectifsFutur;

    @Column(name = "prix_matieres_1er_trimestre")
    private String prixMatieres1erTrimestre; // e.g., "up", "same"

    @Column(name = "prix_matieres_2eme_trimestre")
    private String prixMatieres2emeTrimestre;

    @Column(name = "prix_matieres_futur")
    private String prixMatieresFutur;

    @Column(name = "avoir_difficultes")
    private Boolean avoirDifficultes; // Mapped from "true"/"false"

    @Column(name = "diff_approvisionnement")
    private String diffApprovisionnement;

    @Column(name = "diff_autre")
    private String diffAutre;

    @Column(name = "pleine_capacite")
    private Boolean pleineCapacite; // Mapped from "true"/"false"

    @Column(name = "taux_utilisation_capacite")
    private Integer tauxUtilisationCapacite; // Mapped from number

    @ManyToOne
    @JoinColumn(name = "type_enquete_id", nullable = false)
    private TypeEnquete typeEnquete;
}