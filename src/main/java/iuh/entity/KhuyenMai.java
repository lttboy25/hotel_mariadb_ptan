package iuh.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@ToString(exclude = "hoaDon") // Tránh vòng lặp vô tận khi in log
@Entity
@Table(name = "KhuyenMai")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class KhuyenMai {

    @Id
    @EqualsAndHashCode.Include
    @Column(length = 20) // Giới hạn độ dài mã
    @NotBlank(message = "Mã khuyến mãi không được để trống")
    private String maKhuyenMai;

    @Column(nullable = false, columnDefinition = "nvarchar(255)")
    @NotBlank(message = "Tên khuyến mãi không được để trống")
    private String tenKhuyenMai;

    @Column(nullable = false)
    @NotNull(message = "Ngày bắt đầu không được để trống")
    @FutureOrPresent(message = "Ngày bắt đầu phải là hiện tại hoặc tương lai")
    private LocalDateTime ngayBatDau;

    @Column(nullable = false)
    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDateTime ngayKetThuc;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrangThai trangThai;

    @Column(nullable = false)
    @DecimalMin(value = "0.0", inclusive = true)
    @DecimalMax(value = "1.0", inclusive = true)
    private float heSo; // Ví dụ: 0.1 cho 10%

    @Column(nullable = false)
    @PositiveOrZero(message = "Tổng tiền tối thiểu không được âm")
    private float tongTienToiThieu;

    @Column(nullable = false)
    @PositiveOrZero(message = "Tổng khuyến mãi tối đa không được âm")
    private float tongKhuyenMaiToiDa;

    @Builder.Default
    @OneToMany(mappedBy = "khuyenMai", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<HoaDon> hoaDon = new HashSet<>();

    /**
     * Ràng buộc logic: Ngày kết thúc phải sau ngày bắt đầu
     */
    @AssertTrue(message = "Ngày kết thúc phải sau ngày bắt đầu")
    public boolean isNgayKetThucValid() {
        if (ngayBatDau == null || ngayKetThuc == null) return true;
        return ngayKetThuc.isAfter(ngayBatDau);
    }
}