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
import jakarta.persistence.Persistence;

public class JPAUtil {

    private static final String PERSISTENCE_UNIT_NAME="mariadb-pu";
    private static EntityManagerFactory factory;

    static{
        try{
            factory= Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
    }
    public static EntityManagerFactory getFactory() {
        return factory;
    }
        public static EntityManager getEntityManager(){
            return factory.createEntityManager();
    }

}
