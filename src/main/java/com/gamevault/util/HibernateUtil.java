package com.gamevault.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Singleton pour la SessionFactory Hibernate.
 *
 * NON appelé au démarrage de l'application pour l'instant.
 * Sera activé lors de l'intégration de la couche base de données.
 */
public class HibernateUtil {

    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    sessionFactory = new Configuration()
                            .configure("hibernate.cfg.xml")
                            .buildSessionFactory();
                }
            }
        }
        return sessionFactory;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
