package com.ins.insstatistique.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "enquetes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Enquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "identifiant_stat")
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

    private Boolean exportatrice;

    @Column(name = "variaison_saisoniere")
    private Boolean variaisonSaisoniere;

    @Column(name = "situation_1er_trimestre")
    private String situation1erTrimestre;

    @Column(name = "situation_2eme_trimestre")
    private String situation2emeTrimestre;

    @Column(name = "situation_future")
    private String situationFuture;

    @Column(name = "effectifs_1er_trimestre")
    private String effectifs1erTrimestre;

    @Column(name = "effectifs_2eme_trimestre")
    private String effectifs2emeTrimestre;

    @Column(name = "effectifs_futur")
    private String effectifsFutur;

    @Column(name = "prix_matieres_1er_trimestre")
    private String prixMatieres1erTrimestre;

    @Column(name = "prix_matieres_2eme_trimestre")
    private String prixMatieres2emeTrimestre;

    @Column(name = "prix_matieres_futur")
    private String prixMatieresFutur;

    @Column(name = "avoir_difficultes")
    private Boolean avoirDifficultes;

    @Column(name = "diff_approvisionnement")
    private String diffApprovisionnement;

    @Column(name = "diff_autre")
    private String diffAutre;

    @Column(name = "pleine_capacite")
    private Boolean pleineCapacite;

    @Column(name = "taux_utilisation_capacite")
    private Integer tauxUtilisationCapacite;

    @ManyToOne
    @JoinColumn(name = "type_enquete_id", nullable = false)
    private TypeEnquete typeEnquete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entreprise_id", nullable = false)
    @JsonBackReference("entreprise-enquetes")
    private Entreprise entreprise;


}