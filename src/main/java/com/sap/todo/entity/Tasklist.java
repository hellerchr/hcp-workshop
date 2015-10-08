package com.sap.todo.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Tasklist {
	@Id
	@GeneratedValue
	private long id;

	private String name;
	private String icon;

	@OneToMany(cascade = CascadeType.REMOVE)
	private List<Task> tasks;

	public Tasklist() {
	}

	public Tasklist(String name, String icon) {
		tasks = new ArrayList<>();
		this.name = name;
		this.icon = icon;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public void addTask(Task task) {
		if (task.getTaskList() != this)
			task.setTaskList(this);
		tasks.add(task);
	}
}
