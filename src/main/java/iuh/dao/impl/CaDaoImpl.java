package iuh.dao.impl;

import iuh.dao.CaDao;
import iuh.entity.Ca;

public class CaDaoImpl extends AbstractGenericDaoImpl<Ca, String> implements CaDao {
    public CaDaoImpl() {
        super(Ca.class);
    }
}
