package iuh.dao;

import iuh.db.JPAUtil;
import iuh.dto.HoaDonDTO;
import iuh.entity.HoaDon;
import iuh.mapper.Mapper;
import jakarta.persistence.EntityManager;

import java.util.List;

public class HoaDonDao extends AbstractGenericDaoImpl<HoaDon, String> {

    public HoaDonDao() {super(HoaDon.class);}
    private Mapper mapper;

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

    public HoaDon save(HoaDonDTO hoaDonDTO) {

        HoaDon hoaDon = mapper.map(hoaDonDTO);
        hoaDon.setMaHoaDon(generateMaHoaDon());

        return create(hoaDon);
    }


}
