package com.sap.todo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@XmlRootElement
public class Task {
	@Id
	@GeneratedValue
	private long id;

	private String description;
	private boolean done;

	@ManyToOne
	private Tasklist tasklist;

	public Task() {
	}

	public Task(Tasklist tasklist, String description) {
		this.tasklist = tasklist;
		this.description = description;
		this.done = true;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public String getDescription() {
		return description;
	}

	public boolean isDone() {
		return done;
	}

	public Long getId() {
		return id;
	}

	@XmlTransient
	public Tasklist getTaskList() {
		return tasklist;
	}

	public void setTaskList(Tasklist taskList) {
		this.tasklist = taskList;
	}
}
