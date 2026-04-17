/*
 * @ (#) KhuyenMaiService.java     1.0    4/16/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dao.KhuyenMaiDao;
import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;

import java.util.List;
import java.util.Optional;

public class KhuyenMaiService {

	private final KhuyenMaiDao khuyenMaiDao = new KhuyenMaiDao();

	public List<KhuyenMai> getAllKhuyenMai() {
		return khuyenMaiDao.findAll();
	}

	public List<KhuyenMai> searchKhuyenMai(String keyword) {
		if (keyword == null || keyword.isBlank()) {
			return khuyenMaiDao.findAll();
		}
		return khuyenMaiDao.findByKeyword(keyword);
	}

	public Optional<KhuyenMai> getKhuyenMaiById(String maKhuyenMai) {
		return khuyenMaiDao.findById(maKhuyenMai);
	}

	public List<KhuyenMai> getKhuyenMaiByTrangThai(TrangThai trangThai) {
		return khuyenMaiDao.findByTrangThai(trangThai);
	}

	public String generateNextMaKhuyenMai() {
		return khuyenMaiDao.generateNextMaKhuyenMai();
	}

	public KhuyenMai addKhuyenMai(KhuyenMai khuyenMai) {
		return khuyenMaiDao.save(khuyenMai);
	}

	public KhuyenMai addKhuyenMaiAutoCode(KhuyenMai khuyenMai) {
		for (int i = 0; i < 10; i++) {
			String maKhuyenMai = khuyenMaiDao.generateNextMaKhuyenMai();
			if (khuyenMaiDao.existsById(maKhuyenMai)) {
				continue;
			}
			khuyenMai.setMaKhuyenMai(maKhuyenMai);
			return khuyenMaiDao.save(khuyenMai);
		}
		throw new IllegalStateException("Không thể tạo mã khuyến mãi duy nhất. Vui lòng thử lại.");
	}

	public KhuyenMai updateKhuyenMai(KhuyenMai khuyenMai) {
		return khuyenMaiDao.update(khuyenMai);
	}

	public boolean deleteKhuyenMai(String maKhuyenMai) {
		return khuyenMaiDao.delete(maKhuyenMai);
	}
}
