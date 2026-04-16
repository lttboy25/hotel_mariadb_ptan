/*
 * @ (#) KhuyenMaiDao.java     1.0    4/16/2026
 *
 * Copyright (c) 2026 IUH. All rights reserved.
 */
package iuh.dao;

import iuh.db.JPAUtil;
import iuh.entity.KhuyenMai;
import iuh.entity.TrangThai;
import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class KhuyenMaiDao {

	public List<KhuyenMai> findAll() {
		try (EntityManager em = JPAUtil.getEntityManager()) {
			return em.createQuery("select k from KhuyenMai k order by k.ngayBatDau desc, k.maKhuyenMai asc", KhuyenMai.class)
					.getResultList();
		}
	}

	public Optional<KhuyenMai> findById(String maKhuyenMai) {
		if (maKhuyenMai == null || maKhuyenMai.isBlank()) {
			return Optional.empty();
		}
		try (EntityManager em = JPAUtil.getEntityManager()) {
			return Optional.ofNullable(em.find(KhuyenMai.class, maKhuyenMai));
		}
	}

	public List<KhuyenMai> findByKeyword(String keyword) {
		try (EntityManager em = JPAUtil.getEntityManager()) {
			String q = keyword == null ? "" : keyword.trim().toLowerCase();
			return em.createQuery(
							"select k from KhuyenMai k " +
									"where lower(k.maKhuyenMai) like :q or lower(k.tenKhuyenMai) like :q " +
									"order by k.ngayBatDau desc, k.maKhuyenMai asc",
							KhuyenMai.class)
					.setParameter("q", "%" + q + "%")
					.getResultList();
		}
	}

	public List<KhuyenMai> findByTrangThai(TrangThai trangThai) {
		try (EntityManager em = JPAUtil.getEntityManager()) {
			return em.createQuery(
							"select k from KhuyenMai k where k.trangThai = :trangThai order by k.ngayBatDau desc, k.maKhuyenMai asc",
							KhuyenMai.class)
					.setParameter("trangThai", trangThai)
					.getResultList();
		}
	}

	public KhuyenMai save(KhuyenMai khuyenMai) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(khuyenMai);
			em.getTransaction().commit();
			return khuyenMai;
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
	}

	public KhuyenMai update(KhuyenMai khuyenMai) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			KhuyenMai merged = em.merge(khuyenMai);
			em.getTransaction().commit();
			return merged;
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
	}

	public boolean delete(String maKhuyenMai) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			KhuyenMai entity = em.find(KhuyenMai.class, maKhuyenMai);
			if (entity == null) {
				return false;
			}
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
			return true;
		} catch (RuntimeException ex) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			throw ex;
		} finally {
			em.close();
		}
	}
}
