package iuh.dto;

public enum TrangThaiHoaDon {
    DA_THANH_TOAN,
    CHUA_THANH_TOAN;

    @Override
    public String toString() {
        return switch (this) {
            case DA_THANH_TOAN -> "Đã thanh toán";
            case CHUA_THANH_TOAN -> "Chưa thanh toán";
        };
    }
}
