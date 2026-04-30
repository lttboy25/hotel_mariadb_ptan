package iuh.entity;

public enum TinhTrangPhong {
    TRONG,
    DANG_THUE,
    DANG_O;

    @Override
    public String toString() {
        return switch (this) {
            case TRONG -> "Trống";
            case DANG_THUE -> "Đang thuê";
            case DANG_O -> "Đang ở";
        };
    }
}
