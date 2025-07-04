package com.ins.insstatistique.dto;

/**
 * A record for creating a new Enquete (input DTO)
 */
public record CreateEnqueteRecord(
    String identifiantStat,
    String nomSociale,
    String adresse,
    String telephone,
    String fax,
    String nomRepondant,
    String mailRepondant,
    String brancheActivite,
    Boolean exportatrice,
    Boolean variaisonSaisoniere,
    String situation1erTrimestre,
    String situation2emeTrimestre,
    String situationFuture,
    String effectifs1erTrimestre,
    String effectifs2emeTrimestre,
    String effectifsFutur,
    String prixMatieres1erTrimestre,
    String prixMatieres2emeTrimestre,
    String prixMatieresFutur,
    Boolean avoirDifficultes,
    String diffApprovisionnement,
    String diffAutre,
    Boolean pleineCapacite,
    Integer tauxUtilisationCapacite,
    TypeEnqueteDto typeEnquete
) {

    public record TypeEnqueteDto(Long id){}
}
