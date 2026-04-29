package iuh.dao.impl;

import iuh.db.JPAUtil;
import iuh.dto.HoaDonDTO;
import iuh.entity.HoaDon;
import iuh.mapper.Mapper;
import jakarta.persistence.EntityManager;

public class HoaDonDaoImpl extends AbstractGenericDaoImpl<HoaDon, String> implements iuh.dao.HoaDonDao {

    public HoaDonDaoImpl() {super(HoaDon.class);}

    @Override
    public String generateMaHoaDon() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            String homnay = java.time.LocalDate.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd"));

            String query = """
                SELECT h.maHoaDon FROM HoaDon h
                WHERE h.maHoaDon LIKE :prefix
                ORDER BY h.maHoaDon DESC
                """;

            String maHoaDonCuoi = null;

            try {
                maHoaDonCuoi = em.createQuery(query, String.class)
                        .setParameter("prefix", "HD" + homnay + "%")
                        .setMaxResults(1)
                        .getSingleResult();
            } catch (jakarta.persistence.NoResultException e) {
            }

            int nextNumber = 1;

            if (maHoaDonCuoi != null) {
                String numberPart = maHoaDonCuoi.substring(maHoaDonCuoi.length() - 2);
                nextNumber = Integer.parseInt(numberPart) + 1;
            }

            return String.format("HD%s%02d", homnay, nextNumber);

        } catch (Exception e) {
            System.err.println("Lỗi sinh mã hóa đơn: " + e.getMessage());
            return null;
        } finally {
            em.close();
        }
    }

    @Override
    public HoaDon save(HoaDonDTO hoaDonDTO) {

        HoaDon hoaDon = Mapper.map(hoaDonDTO);
        if (hoaDon.getMaHoaDon() == null || hoaDon.getMaHoaDon().isBlank()) {
            hoaDon.setMaHoaDon(generateMaHoaDon());
        }

        return create(hoaDon);
    }


}
