package com.sap.todo;

import java.util.HashMap;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.glassfish.hk2.api.Factory;

public class EMFactory implements Factory<EntityManager> {

	public static final String JNDI_DB = "java:comp/env/jdbc/DefaultDB";
	public static final String PERSISTENCE_UNIT = "application";

	private static EntityManagerFactory emf;

	public EntityManager provide() {
		return getEntityManagerFactory().createEntityManager();
	}

	public void dispose(EntityManager entityManager) {
		entityManager.close();
	}

	public static EntityManagerFactory getEntityManagerFactory() {
		if (emf == null) {
			try {
				InitialContext ctx = new InitialContext();
				DataSource ds = (DataSource) ctx.lookup(JNDI_DB);

				Map<Object, Object> properties = new HashMap<>();
				properties.put(PersistenceUnitProperties.NON_JTA_DATASOURCE, ds);
				properties.put(PersistenceUnitProperties.WEAVING_INTERNAL, "false");

				emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);

			} catch (NamingException e) {
				throw new RuntimeException(e);
			}
		}
		return emf;
	}
}