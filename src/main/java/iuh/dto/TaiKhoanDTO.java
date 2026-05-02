package iuh.dto;

import lombok.*;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Builder
public class TaiKhoanDTO implements Serializable {
    private String maNhanVien;
    private String matKhau;
    private String vaiTro;
}