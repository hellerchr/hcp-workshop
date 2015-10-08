package com.sap.todo.dao;

import javax.persistence.NoResultException;

import com.sap.todo.entity.Task;

public class TaskDAO extends BaseDAO<Task, Long> {
	public TaskDAO() {
		super(Task.class);
	}
	
	public Task findTaskByListIdAndTaskId(long tasklistId, long taskId) {
		try {
			Task task = (Task) em.createQuery("select t from Task t where t.tasklist.id = :tasklistId and t.id = :taskId")
						.setParameter("tasklistId", tasklistId)
						.setParameter("taskId", taskId)
						.getSingleResult();
			return task;
		}
		catch (NoResultException e) {
			return null;
		}
	}
}
