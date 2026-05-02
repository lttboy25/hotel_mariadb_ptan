package iuh.enums;

import lombok.Getter;

@Getter
public enum TrangThaiNhanVien {
    DANG_LAM("Đang làm việc"),
    MOI_VAO("Mới vào"),
    THU_VIEC("Thử việc"),
    DA_NGHI("Đã nghỉ");

    private final String display;

    TrangThaiNhanVien(String display) {
        this.display = display;
    }

    @Override
    public String toString() {
        return display;
    }
}
