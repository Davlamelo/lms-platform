package com.formation.lms.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton qui gère le pool de connexions HikariCP.
 * Charge la config depuis db.properties.
 *
 * BOILERPLATE : cette classe est réutilisable dans tous tes projets JDBC.
 * Tu changes juste le fichier db.properties.
 *
 * Usage :
 *   Connection conn = DBConnection.getConnection();
 *   // ... utiliser conn ...
 *   conn.close(); // rend la connexion au pool (ne la ferme pas vraiment)
 */
public class DBConnection {

    // Instance unique du pool (Singleton)
    private static HikariDataSource dataSource;

    // Bloc statique : exécuté UNE SEULE FOIS au chargement de la classe
    static {
        try {
            // 1. Charger le fichier db.properties depuis le classpath
            Properties proprietes = new Properties();
            InputStream flux = DBConnection.class.getClassLoader()
                    .getResourceAsStream("db.properties");

            if (flux == null) {
                throw new RuntimeException("Fichier db.properties introuvable dans le classpath !");
            }

            proprietes.load(flux);
            flux.close();

            // 2. Configurer HikariCP avec les propriétés
            HikariConfig config = new HikariConfig();
            config.setDriverClassName(proprietes.getProperty("db.driver"));
            config.setJdbcUrl(proprietes.getProperty("db.url"));
            config.setUsername(proprietes.getProperty("db.username"));
            config.setPassword(proprietes.getProperty("db.password"));

            // Paramètres du pool
            config.setMaximumPoolSize(
                    Integer.parseInt(proprietes.getProperty("hikari.maximumPoolSize", "10")));
            config.setMinimumIdle(
                    Integer.parseInt(proprietes.getProperty("hikari.minimumIdle", "2")));
            config.setIdleTimeout(
                    Long.parseLong(proprietes.getProperty("hikari.idleTimeout", "30000")));
            config.setConnectionTimeout(
                    Long.parseLong(proprietes.getProperty("hikari.connectionTimeout", "30000")));
            config.setMaxLifetime(
                    Long.parseLong(proprietes.getProperty("hikari.maxLifetime", "1800000")));
            config.setPoolName(proprietes.getProperty("hikari.poolName", "LMS-Pool"));

            // 3. Créer le pool
            dataSource = new HikariDataSource(config);

            System.out.println("✅ Pool de connexions HikariCP initialisé ("
                    + config.getPoolName() + ")");

        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement de db.properties", e);
        }
    }

    /**
     * Obtient une connexion du pool.
     * IMPORTANT : toujours fermer avec conn.close() (try-with-resources recommandé).
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Ferme le pool de connexions (appelé à l'arrêt de l'application).
     */
    public static void fermerPool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println(" Pool de connexions fermé.");
        }
    }

    // Constructeur privé : empêche l'instanciation (pattern Singleton)
    private DBConnection() {
    }
}