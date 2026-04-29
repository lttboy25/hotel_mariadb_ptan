/*
 * @ (#) DatPhongDao.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.dto.DatPhongRequestDTO;
import iuh.entity.PhieuDatPhong;
import iuh.entity.Phong;
import jakarta.persistence.EntityManager;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface DatPhongDao {
    PhieuDatPhong saveDatPhong(DatPhongRequestDTO request);

    default List<Integer> allocatePeoplePerRoom(List<Phong> rooms, int adults, int children) {
        int n = rooms.size();
        int[] adultsAlloc = new int[n];
        int[] childrenAlloc = new int[n];

        int remainingAdults = adults;
        int remainingChildren = children;

        // Bước 1: Phân bổ ít nhất 1 người lớn vào mỗi phòng nếu có thể
        for (int i = 0; i < n && remainingAdults > 0; i++) {
            Phong room = rooms.get(i);
            int maxAdults = room.getLoaiPhong() != null ? room.getLoaiPhong().getSoNguoiLonToiDa() : 0;
            if (maxAdults > 0) {
                adultsAlloc[i] = 1;
                remainingAdults--;
            }
        }

        // Bước 2: Phân bổ số người lớn còn lại vào các phòng cho đến khi đạt sức chứa tối đa
        for (int i = 0; i < n && remainingAdults > 0; i++) {
            Phong room = rooms.get(i);
            int maxAdults = room.getLoaiPhong() != null ? room.getLoaiPhong().getSoNguoiLonToiDa() : 0;
            int canTake = maxAdults - adultsAlloc[i];
            int toAdd = Math.min(remainingAdults, canTake);
            adultsAlloc[i] += toAdd;
            remainingAdults -= toAdd;
        }

        // Bước 3: Phân bổ trẻ em vào các phòng
        for (int i = 0; i < n && remainingChildren > 0; i++) {
            Phong room = rooms.get(i);
            int maxChildren = room.getLoaiPhong() != null ? room.getLoaiPhong().getSoTreEmToiDa() : 0;
            int toAdd = Math.min(remainingChildren, maxChildren);
            childrenAlloc[i] += toAdd;
            remainingChildren -= toAdd;
        }

        List<Integer> allocation = new java.util.ArrayList<>();
        for (int i = 0; i < n; i++) {
            allocation.add(adultsAlloc[i] + childrenAlloc[i]);
        }

        if (remainingAdults > 0 || remainingChildren > 0) {
            throw new IllegalStateException("Không đủ sức chứa để phân bổ số người theo phòng.");
        }
        return allocation;
    }

    default String generateNextMaPhieuDatPhong(EntityManager em) {
        Number maxNumber = (Number) em.createNativeQuery(
                        "SELECT MAX(CAST(SUBSTRING(maPhieuDatPhong, 4) AS UNSIGNED)) FROM PhieuDatPhong FOR UPDATE")
                .getSingleResult();
        int nextNumber = (maxNumber == null) ? 1 : maxNumber.intValue() + 1;
        return String.format("PDP%03d", nextNumber);
    }

    default boolean isRoomAvailable(EntityManager em, String maPhong, LocalDateTime checkIn, LocalDateTime checkOut) {
        Long count = em.createQuery("""
                
                        SELECT COUNT(ct) FROM ChiTietPhieuDatPhong ct
                WHERE ct.phong.maPhong = :maPhong
                AND ct.thoiGianNhanPhong < :checkOut
                AND ct.thoiGianTraPhong > :checkIn
                AND ct.trangThai != 'Đã hủy'
                """, Long.class)
                .setParameter("maPhong", maPhong)
                .setParameter("checkIn", checkIn)
                .setParameter("checkOut", checkOut)
                .getSingleResult();
        return count == 0;
    }

    default int calculateHours(LocalDateTime checkIn, LocalDateTime checkOut) {
        long hours = ChronoUnit.HOURS.between(checkIn, checkOut);
        return (int) Math.max(1, hours);
    }
}
