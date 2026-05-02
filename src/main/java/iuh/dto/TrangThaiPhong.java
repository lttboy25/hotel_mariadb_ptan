package iuh.dto;

public enum TrangThaiPhong {
    SAN_SANG,
    TOT,
    DANG_SUA_CHUA,
    DANG_DON_DEP;

    @Override
    public String toString() {
        return switch (this) {
            case SAN_SANG -> "Sẵn sàng";
            case TOT -> "Tốt";
            case DANG_SUA_CHUA -> "Đang sửa chữa";
            case DANG_DON_DEP -> "Đang dọn dẹp";
        };
    }
}
