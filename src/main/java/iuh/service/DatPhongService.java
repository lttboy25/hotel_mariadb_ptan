/*
 * @ (#) DatPhongService.java     1.0    4/20/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;


/*
 * @description
 * @author:NguyenTruong
 * @date:  4/20/2026
 * @version:    1.0
 */

import iuh.dao.DatPhongDao;
import iuh.entity.Phong;

import java.util.List;

public class DatPhongService {
    DatPhongDao datPhongDao;

    public DatPhongService() {
        this.datPhongDao = new DatPhongDao();
    }
    public List<Phong> getDsPhongTrong(){
        return datPhongDao.getDsPhongTrong();
    }
}
