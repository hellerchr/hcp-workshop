package com.sap.todo.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

abstract public class BaseDAO<TYPE, KEY> {

	@Inject
	protected EntityManager em;
	protected final Class<TYPE> type;

	public BaseDAO(Class<TYPE> clz) {
		this.type = clz;
	}

	@SuppressWarnings("unchecked")
	public List<TYPE> findAll() {
		return em.createQuery("SELECT x FROM " + this.type.getSimpleName() + " x").getResultList();
	}

	public TYPE find(KEY key) {
		return em.find(type, key);
	}

	public TYPE merge(TYPE entity) {
		em.getTransaction().begin();
		try {
			TYPE e = em.merge(entity);
			em.getTransaction().commit();
			return e;
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	public TYPE persist(TYPE entity) {
		try {
			em.getTransaction().begin();
			em.persist(entity);
			em.getTransaction().commit();
			return entity;
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	public TYPE remove(TYPE entity) {
		em.getTransaction().begin();
		try {
			entity = em.merge(entity);
			em.remove(entity);
			em.getTransaction().commit();
			return entity;
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	public List<TYPE> remove(List<TYPE> entities) {
		em.getTransaction().begin();
		try {
			for (TYPE entity : entities) {
				entity = em.merge(entity);
				em.remove(entity);
			}
			em.getTransaction().commit();
			return entities;
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	public TYPE removeByKey(KEY key) {
		em.getTransaction().begin();
		try {
			TYPE entity = em.find(type, key);
			if (entity != null) {
				em.remove(entity);
				em.getTransaction().commit();
			}
			return entity;
		} finally {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
		}
	}

	public boolean exists(KEY key) {
		return find(key) != null;
	}

	public Long count() {
		return (Long) em.createQuery("SELECT COUNT(x) FROM " + this.type.getSimpleName() + " x").getSingleResult();
	}

	public void setEntityManager(EntityManager em) {
		this.em = em;
	}

}