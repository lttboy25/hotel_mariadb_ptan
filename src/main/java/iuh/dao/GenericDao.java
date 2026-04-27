/*
 * @ (#) GenericDao.java     1.0    4/17/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/17/2026
 * @version:    1.0
 */
public interface GenericDao <T,ID>{
    T create(T t);
    T update(T t);
    boolean delete(ID id);
    List<T> loadAll();

}
