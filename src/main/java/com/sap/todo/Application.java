package com.sap.todo;

import javax.persistence.EntityManager;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UserProvider;
import com.sap.todo.dao.TaskDAO;
import com.sap.todo.dao.TaskListDAO;

public class Application extends ResourceConfig {

	private String applicationRootPackage;

	public Application() {
		this.applicationRootPackage = this.getClass().getPackage().getName();
		registerEndpoints();
		registerDependecyInjection();
	}

	private void registerEndpoints() {
		// scan whole root package recursively
		packages(true, applicationRootPackage);
	}

	private void registerDependecyInjection() {
		register(new AbstractBinder() {
			@Override
			protected void configure() {
				// UserProvider
				bind(getUserProvider()).to(UserProvider.class);

				// EMF
				bindFactory(EMFactory.class).to(EntityManager.class).in(RequestScoped.class);

				// DAOs
				bind(TaskDAO.class).to(TaskDAO.class).in(RequestScoped.class);
				bind(TaskListDAO.class).to(TaskListDAO.class).in(RequestScoped.class);
			}

		});
	}

	private UserProvider getUserProvider() {
		try {
			return UserManagementAccessor.getUserProvider();
		} catch (PersistenceException e) {
			throw new RuntimeException(e);
		}
	}
}
