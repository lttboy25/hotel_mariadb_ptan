package iuh.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDate;

@Entity
@Table(name = "KhachHang")
public class KhachHang {
    @Id
    @Column(name = "maKhachHang", nullable = false, length = 20)
    private String maKhachHang;

    @Column(name = "CCCD", length = 20)
    private String CCCD;

    @Column(name = "hoTen", nullable = false, length = 100)
    private String tenKhachHang;

    @Column(name = "soDienThoai", length = 15)
    private String soDienThoai;

    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "ngayTao")
    private LocalDate ngayTao;

    public KhachHang() {
    }

    public KhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public KhachHang(String maKhachHang, String CCCD, String tenKhachHang, String soDienThoai, String email,
            LocalDate ngayTao) {
        this.maKhachHang = maKhachHang;
        this.CCCD = CCCD;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.email = email;
        this.ngayTao = ngayTao;
    }

    public KhachHang(String maKhachHang, String CCCD, String tenKhachHang, String soDienThoai, String email) {
        this.maKhachHang = maKhachHang;
        this.CCCD = CCCD;
        this.tenKhachHang = tenKhachHang;
        this.soDienThoai = soDienThoai;
        this.email = email;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }
}
