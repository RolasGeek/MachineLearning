package com.studies.service;

import java.awt.print.Book;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.studies.emf.EntityManagerFactoryIns;
import com.studies.model.Messages;

public class MessageService {
	private EntityManager em;
	private static MessageService instance;
	public static MessageService getInstance() {
		instance = new MessageService();
		instance.setEm(EntityManagerFactoryIns.getInstance().getEntityManagerFactory().createEntityManager());
		return instance;
	}
	
	public void create(Messages entity) {
		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		em.close();
	}

	public void deleteById(Integer id) {
		Messages entity = em.find(Messages.class, id);
		if (entity != null) {
			em.getTransaction().begin();
			em.remove(entity);
			em.getTransaction().commit();
			em.close();
		}
	}

	public Messages findById(Integer id) {
		return em.find(Messages.class, id);
	}

	public Messages update(Messages entity) {
		return em.merge(entity);
	}
	public List<String> findUniquenames() {
		em.getTransaction().begin();
		Query q = em.createQuery("select m.name from Messages m group by m.name");
		List<String> users = q.getResultList();
		em.getTransaction().commit();
		em.close();
		return users;
	}
	public Integer countByName(String name) {
		em.getTransaction().begin();
		Query q = em.createQuery("select m from Messages m where m.name= ?1", Messages.class);
		q.setParameter(1, name);
		List<Messages> result = (List<Messages>)  q.getResultList();
		em.getTransaction().commit();
		em.close();
		return  result.size();
	}

	public List<Messages> listAll(Integer startPosition, Integer maxResult, String order) {
		em.getTransaction().begin();
		TypedQuery<Messages> findAllQuery = em.createQuery(
				"SELECT DISTINCT m FROM Messages m ORDER BY m.id "+order,
				Messages.class);
		if (startPosition != null) {
			findAllQuery.setFirstResult(startPosition);
		}
		if (maxResult != null) {
			findAllQuery.setMaxResults(maxResult);
		}
		em.getTransaction().commit();
		List<Messages> m= findAllQuery.getResultList();
		em.close();
		return m;
	}
	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}
}
