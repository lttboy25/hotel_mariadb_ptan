/*
 * @ (#) KhuyenMaiDTO.java     1.0    4/25/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import iuh.entity.TrangThai;

/*
 * @description
 * @author:NguyenTruong
 * @date:  4/25/2026
 * @version:    1.0
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KhuyenMaiDTO implements Serializable {
    private String maKhuyenMai;
    private String tenKhuyenMai;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private TrangThai trangThai;
    private float heSo;
    private float tongTienToiThieu;
    private float tongKhuyenMaiToiDa;
}
