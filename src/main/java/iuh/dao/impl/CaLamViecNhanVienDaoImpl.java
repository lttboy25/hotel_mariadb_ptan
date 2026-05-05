package iuh.dao.impl;

import iuh.dao.CaLamViecNhanVienDao;
import iuh.entity.CaLamViecNhanVien;
import jakarta.persistence.NoResultException;

import java.util.List;

public class CaLamViecNhanVienDaoImpl extends AbstractGenericDaoImpl<CaLamViecNhanVien, String> implements CaLamViecNhanVienDao {

    public CaLamViecNhanVienDaoImpl() {
        super(CaLamViecNhanVien.class);
    }

    @Override
    public List<CaLamViecNhanVien> findByNhanVien(String maNhanVien) {
        return doInTransaction(em -> {
            return em.createQuery("SELECT c FROM CaLamViecNhanVien c WHERE c.nhanVien.maNhanVien = :maNhanVien ORDER BY c.ngay DESC", CaLamViecNhanVien.class)
                    .setParameter("maNhanVien", maNhanVien)
                    .getResultList();
        });
    }

    @Override
    public CaLamViecNhanVien findActiveShift(String maNhanVien) {
        return doInTransaction(em -> {
            try {
                return em.createQuery("SELECT c FROM CaLamViecNhanVien c WHERE c.nhanVien.maNhanVien = :maNhanVien AND c.trangThai = 'ĐANG_LAM'", CaLamViecNhanVien.class)
                        .setParameter("maNhanVien", maNhanVien)
                        .getSingleResult();
            } catch (NoResultException e) {
                return null;
            }
        });
    }

    @Override
    public List<CaLamViecNhanVien> findCompletedShifts(String maNhanVien) {
        return doInTransaction(em -> {
            return em.createQuery("SELECT c FROM CaLamViecNhanVien c WHERE c.nhanVien.maNhanVien = :maNhanVien AND c.trangThai = 'DA_KET_THUC' ORDER BY c.ngay DESC", CaLamViecNhanVien.class)
                    .setParameter("maNhanVien", maNhanVien)
                    .getResultList();
        });
    }
}
