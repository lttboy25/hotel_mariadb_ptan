package iuh.dto;

public enum TrangThaiPhieuDatPhong {
    DA_THANH_TOAN,
    NHAN_PHONG,
    CHUA_THANH_TOAN,
    DA_DAT,
    DA_HUY;

    @Override
    public String toString() {
        return switch (this) {
            case DA_DAT -> "Đã đặt";
            case DA_THANH_TOAN -> "Đã thanh toán";
            case NHAN_PHONG -> "Nhận phòng";
            case CHUA_THANH_TOAN -> "Chưa thanh toán";
            case DA_HUY -> "Đã hủy";
        };
    }
}
