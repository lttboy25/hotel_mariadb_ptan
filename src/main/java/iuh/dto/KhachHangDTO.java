/*
 * @ (#) KhachHangDTO.java     1.0    4/23/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

/*
 * @description Data Transfer Object cho KhachHang
 * @author: NguyenTruong
 * @date:  4/23/2026
 * @version: 1.0
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class KhachHangDTO implements Serializable {
    private String maKhachHang;
    private String CCCD;
    private String tenKhachHang;
    private String soDienThoai;
    private String email;
    private LocalDate ngayTao;
}
