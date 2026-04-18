package iuh.dao;

import iuh.entity.KhachHang;
import java.util.List;

public class KhachHangDao extends AbstractGenericDaoImpl<KhachHang, String> {
    public KhachHangDao() {
        super(KhachHang.class);
    }

    // Tự động phát sinh mã khách hàng mới theo định dạng KHxxx [cite: 245]
    public String generateNextMaKH() {
        List<String> allCodes = doInTransaction(em ->
                em.createQuery("select k.maKhachHang from KhachHang k", String.class).getResultList());

        int nextNumber = allCodes.stream()
                .map(code -> Integer.parseInt(code.replaceAll("\\D+", "")))
                .max(Integer::compareTo).orElse(0) + 1;

        return String.format("KH%03d", nextNumber);
    }

    // Tìm kiếm khách hàng theo từ khóa (tên hoặc số điện thoại)
    public List<KhachHang> findByKeyword(String keyword) {
        String kw = keyword == null ? "" : keyword.trim().toLowerCase();
        return doInTransaction(em -> em.createQuery(
                        "SELECT k FROM KhachHang k WHERE LOWER(k.tenKhachHang) LIKE :kw OR k.soDienThoai LIKE :kw",
                        KhachHang.class)
                .setParameter("kw", "%" + kw + "%")
                .getResultList());
    }
}