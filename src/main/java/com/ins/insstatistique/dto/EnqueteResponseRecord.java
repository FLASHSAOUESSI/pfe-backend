package com.ins.insstatistique.dto;

/**
 * A record for representing an Enquete in API responses (output DTO)
 */
public record EnqueteResponseRecord(
    Long id,
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
    EnterpriseSimpleRecord enterprise
) {
}
