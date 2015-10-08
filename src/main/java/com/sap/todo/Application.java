package com.sap.todo;

import javax.persistence.EntityManager;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;

import com.sap.security.um.service.UserManagementAccessor;
import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.UserProvider;


public class Application extends ResourceConfig {

	private String applicationRootPackage;

	public Application() {
		this.applicationRootPackage = this.getClass().getPackage().getName();
		registerEndpoints();
		registerDependecyInjection();
		myMain();
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

	private void myMain() {
		/*
		EntityManager em = EMFactory.getEntityManagerFactory().createEntityManager();		
		
		TaskDAO taskDAO = new TaskDAO();
		taskDAO.setEntityManager(em);
		
		TaskListDAO taskListDAO = new TaskListDAO();
		taskListDAO.setEntityManager(em);
		
		Tasklist tasklist = new Tasklist();
		tasklist.setName("Shopping");
		tasklist.setId(99);
		taskListDAO.persist(tasklist);
		
		Task task = new Task();
		task.setId(100);
		task.setTaskList(tasklist);
		task.setDescription("Buy Milk");
		tasklist.getTasks().add(task);
		taskDAO.persist(task);
		
		Task searchedTask = taskDAO.findTaskByListIdAndTaskId(99, 100);
		boolean done = searchedTask.isDone();
		
		em.close();
		*/
	}
}
