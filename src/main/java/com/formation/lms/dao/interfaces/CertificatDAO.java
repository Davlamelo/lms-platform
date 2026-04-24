package com.formation.lms.dao.interfaces;

import com.formation.lms.models.Certificat;

import java.sql.SQLException;
import java.util.Optional;

/**
 * Interface DAO pour les certificats.
 */
public interface CertificatDAO extends GenericDAO<Certificat, Long> {

    /**
     * Trouve le certificat d'une inscription.
     */
    Optional<Certificat> findByInscriptionId(Long inscriptionId) throws SQLException;

    /**
     * Vérifie l'authenticité d'un certificat par son code.
     */
    Optional<Certificat> findByCodeVerification(String code) throws SQLException;
}