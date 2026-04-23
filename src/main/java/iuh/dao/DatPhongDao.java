/*
 * @ (#) DatPhongDao.java     1.0    4/20/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;


/*
 * @description
 * @author:NguyenTruong
 * @date:  4/20/2026
 * @version:    1.0
 */

import iuh.entity.Phong;

import java.util.List;

public class DatPhongDao {

    public List<Phong>getDsPhongTrong(){
        PhongDao phongDao = new PhongDao();
        return phongDao.findPhongTrong();
    }

    public static void main(String[] args) {
            DatPhongDao datPhongDao = new DatPhongDao();
            List<Phong> dsPhongTrong = datPhongDao.getDsPhongTrong();
            System.out.println("Danh sách phòng trống:");
            for (Phong phong : dsPhongTrong) {
                System.out.println(phong.getMaPhong());
            }
    }
}
