package com.studies.emf;
 

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactoryIns {
	private EntityManagerFactory entityManagerFactory;
	private static EntityManagerFactoryIns instance;
	
	public static EntityManagerFactoryIns getInstance() {
		if(instance == null) {
			instance = new EntityManagerFactoryIns();
			instance.setEntityManagerFactory(Persistence.createEntityManagerFactory("MachineLearning-persistence-unit"));
		}
		return instance;
		// TODO Auto-generated constructor stub
	}
	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}
	public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}
	
}
