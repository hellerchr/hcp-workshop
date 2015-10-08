package com.sap.todo.dao;

import com.sap.todo.entity.Tasklist;

public class TaskListDAO extends BaseDAO<Tasklist, Long> {
	public TaskListDAO() {
		super(Tasklist.class);
	}
}
