/*
 * @ (#) KhuyenMaiServiceImpl.java     1.0    4/16/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.service.impl;

import iuh.dao.impl.KhuyenMaiDaoImpl;
import iuh.dto.KhuyenMaiDTO;
import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;
import iuh.mapper.Mapper;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class KhuyenMaiServiceImpl implements iuh.service.KhuyenMaiService, Serializable {

	private final KhuyenMaiDaoImpl khuyenMaiDao = new KhuyenMaiDaoImpl();

	@Override
	public List<KhuyenMaiDTO> getAllKhuyenMai() {
		return khuyenMaiDao.findAll().stream()
				.map(Mapper::map)
				.collect(Collectors.toList());
	}

	@Override
	public List<KhuyenMaiDTO> searchKhuyenMai(String keyword) {
		List<KhuyenMai> list = (keyword == null || keyword.isBlank())
				? khuyenMaiDao.findAll()
				: khuyenMaiDao.findByKeyword(keyword);
		return list.stream().map(Mapper::map).collect(Collectors.toList());
	}

	@Override
	public Optional<KhuyenMaiDTO> getKhuyenMaiById(String maKhuyenMai) {
		return khuyenMaiDao.findById(maKhuyenMai).map(Mapper::map);
	}

	@Override
	public List<KhuyenMaiDTO> getKhuyenMaiByTrangThai(TrangThai trangThai) {
		return khuyenMaiDao.findByTrangThai(trangThai).stream()
				.map(Mapper::map)
				.collect(Collectors.toList());
	}

	@Override
	public String generateNextMaKhuyenMai() {
		return khuyenMaiDao.generateNextMaKhuyenMai();
	}

	@Override
	public KhuyenMaiDTO addKhuyenMai(KhuyenMaiDTO dto) {
		KhuyenMai khuyenMai = Mapper.map(dto);
		return Mapper.map(khuyenMaiDao.save(khuyenMai));
	}

	@Override
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

	@Override
	public KhuyenMaiDTO updateKhuyenMai(KhuyenMaiDTO dto) {
		KhuyenMai khuyenMai = Mapper.map(dto);
		return Mapper.map(khuyenMaiDao.update(khuyenMai));
	}

	@Override
	public boolean deleteKhuyenMai(String maKhuyenMai) {
		return khuyenMaiDao.delete(maKhuyenMai);
	}
}
