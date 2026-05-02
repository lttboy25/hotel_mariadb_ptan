package iuh.dao.impl;

import iuh.entity.KhachHang;
import java.util.List;

public class KhachHangDaoImpl extends AbstractGenericDaoImpl<KhachHang, String> implements iuh.dao.KhachHangDao {
    public KhachHangDaoImpl() {
        super(KhachHang.class);
    }

    // Tự động phát sinh mã khách hàng mới theo định dạng KHxxx [cite: 245]
    @Override
    public String generateNextMaKH() {
        List<String> allCodes = doInTransaction(em ->
                em.createQuery("select k.maKhachHang from KhachHang k", String.class).getResultList());

        int nextNumber = allCodes.stream()
                .map(code -> Integer.parseInt(code.replaceAll("\\D+", "")))
                .max(Integer::compareTo).orElse(0) + 1;

        return String.format("KH%03d", nextNumber);
    }

    // Tìm kiếm khách hàng theo từ khóa (tên hoặc số điện thoại)
    @Override
    public List<KhachHang> findByKeyword(String keyword) {
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        return doInTransaction(em -> em.createQuery(
                        "SELECT k FROM KhachHang k WHERE LOWER(k.tenKhachHang) LIKE :kw OR k.soDienThoai LIKE :kw",
                        KhachHang.class)
                .setParameter("kw", "%" + kw + "%")
                .getResultList());
    }
    @Override
    public KhachHang findByCCCD(String cccd) {
        String value = cccd == null ? "" : cccd.trim();
        if (value.isEmpty()) return null;

        return doInTransaction(em -> em.createQuery(
                        "SELECT k FROM KhachHang k WHERE k.CCCD = :cccd",
                        KhachHang.class)
                .setParameter("cccd", value)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null));
    }


}