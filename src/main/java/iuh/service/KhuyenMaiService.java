/*
 * @ (#) KhuyenMaiService.java     1.0    4/16/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service;

import iuh.dao.impl.KhuyenMaiDaoImpl;
import iuh.dto.KhuyenMaiDTO;
import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;
import iuh.mapper.Mapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KhuyenMaiService {

	private final KhuyenMaiDaoImpl khuyenMaiDao = new KhuyenMaiDaoImpl();

	public List<KhuyenMaiDTO> getAllKhuyenMai() {
		return khuyenMaiDao.findAll().stream()
				.map(Mapper::map)
				.collect(Collectors.toList());
	}

	public List<KhuyenMaiDTO> searchKhuyenMai(String keyword) {
		List<KhuyenMai> list = (keyword == null || keyword.isBlank())
				? khuyenMaiDao.findAll()
				: khuyenMaiDao.findByKeyword(keyword);
		return list.stream().map(Mapper::map).collect(Collectors.toList());
	}

	public Optional<KhuyenMaiDTO> getKhuyenMaiById(String maKhuyenMai) {
		return khuyenMaiDao.findById(maKhuyenMai).map(Mapper::map);
	}

	public List<KhuyenMaiDTO> getKhuyenMaiByTrangThai(TrangThai trangThai) {
		return khuyenMaiDao.findByTrangThai(trangThai).stream()
				.map(Mapper::map)
				.collect(Collectors.toList());
	}

	public String generateNextMaKhuyenMai() {
		return khuyenMaiDao.generateNextMaKhuyenMai();
	}

	public KhuyenMaiDTO addKhuyenMai(KhuyenMaiDTO dto) {
		KhuyenMai khuyenMai = Mapper.map(dto);
		return Mapper.map(khuyenMaiDao.save(khuyenMai));
	}

	public KhuyenMaiDTO addKhuyenMaiAutoCode(KhuyenMaiDTO dto) {
		KhuyenMai khuyenMai = Mapper.map(dto);
		for (int i = 0; i < 10; i++) {
			String maKhuyenMai = khuyenMaiDao.generateNextMaKhuyenMai();
			if (khuyenMaiDao.existsById(maKhuyenMai)) {
				continue;
			}
			khuyenMai.setMaKhuyenMai(maKhuyenMai);
			return Mapper.map(khuyenMaiDao.save(khuyenMai));
		}
		throw new IllegalStateException("Không thể tạo mã khuyến mãi duy nhất. Vui lòng thử lại.");
	}

	public KhuyenMaiDTO updateKhuyenMai(KhuyenMaiDTO dto) {
		KhuyenMai khuyenMai = Mapper.map(dto);
		return Mapper.map(khuyenMaiDao.update(khuyenMai));
	}

	public boolean deleteKhuyenMai(String maKhuyenMai) {
		return khuyenMaiDao.delete(maKhuyenMai);
	}
}
