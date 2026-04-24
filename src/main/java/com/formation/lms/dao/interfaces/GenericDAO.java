package com.formation.lms.dao.interfaces;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Interface générique pour les opérations CRUD de base.
 * Toutes les interfaces DAO héritent de cette interface.
 *
 * @param <T>  le type de l'entité (ex: Utilisateur, Cours)
 * @param <ID> le type de la clé primaire (ex: Long)
 *
 * PATTERN : c'est un pattern récurrent. Cette interface est identique
 * dans 95% des projets Java. Seuls les types T et ID changent.
 */
public interface GenericDAO<T, ID> {

    /**
     * Trouve une entité par son identifiant.
     * @param id l'identifiant de l'entité
     * @return Optional contenant l'entité si trouvée, vide sinon
     */
    Optional<T> findById(ID id) throws SQLException;

    /**
     * Récupère toutes les entités de la table.
     * @return liste de toutes les entités
     */
    List<T> findAll() throws SQLException;

    /**
     * Persiste une nouvelle entité en base de données.
     * @param entite l'entité à sauvegarder
     * @return l'entité avec son ID généré
     */
    T save(T entite) throws SQLException;

    /**
     * Met à jour une entité existante.
     * @param entite l'entité à mettre à jour
     * @return true si la mise à jour a réussi
     */
    boolean update(T entite) throws SQLException;

    /**
     * Supprime une entité par son identifiant.
     * @param id l'identifiant de l'entité à supprimer
     * @return true si la suppression a réussi
     */
    boolean delete(ID id) throws SQLException;

    /**
     * Compte le nombre total d'entités dans la table.
     * @return le nombre d'entités
     */
    long count() throws SQLException;
}