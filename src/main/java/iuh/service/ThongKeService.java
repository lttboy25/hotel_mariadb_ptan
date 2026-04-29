/*
 * @ (#) ThongKeService.java     1.0    4/29/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dto.ThongKeDTO;

import java.time.LocalDate;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/29/2026
 * @version:    1.0
 */
public interface ThongKeService {
    ThongKeDTO layThongKe(LocalDate tuNgay, LocalDate denNgay);
}
