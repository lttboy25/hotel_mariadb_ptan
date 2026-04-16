/*
 * @ (#) JPAUtil.java     1.0    4/13/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.db;


/*
 * @description
 * @author:NguyenTruong
 * @date:  4/13/2026
 * @version:    1.0
 */

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.jpa.HibernatePersistenceProvider;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME = "mariadb-pu";
    private static volatile EntityManagerFactory factory;

    private JPAUtil() {
    }

    public static EntityManagerFactory getFactory() {
        if (factory == null) {
            synchronized (JPAUtil.class) {
                if (factory == null) {
                    factory = new HibernatePersistenceProvider()
                            .createEntityManagerFactory(PERSISTENCE_UNIT_NAME, null);
                    if (factory == null) {
                        throw new IllegalStateException("Không thể khởi tạo EntityManagerFactory cho persistence unit: " + PERSISTENCE_UNIT_NAME);
                    }
                }
            }
        }
        return factory;
    }

    public static EntityManager getEntityManager(){
            return getFactory().createEntityManager();
    }

}
