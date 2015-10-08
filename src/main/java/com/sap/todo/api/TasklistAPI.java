package com.sap.todo.api;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.sap.todo.dao.TaskDAO;
import com.sap.todo.dao.TaskListDAO;
import com.sap.todo.entity.Task;
import com.sap.todo.entity.Tasklist;

@Path("/tasklist")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TasklistAPI {
	@Inject
	TaskListDAO taskListDAO;

	@Inject
	TaskDAO taskDAO;

	@GET
	@Path("/")
	public List<Tasklist> readAllTasklists() {
		return taskListDAO.findAll();
	}


	@POST
	@Path("/")
	public Tasklist createTaskList(Tasklist tasklist) {
		return taskListDAO.persist(tasklist);
	}

	@POST
	@Path("/{id}/task/")
	public Task createTask(@PathParam("id") long listId, Task task) {
		Tasklist taskList = taskListDAO.find(listId);

		if (taskList == null)
			throw new NotFoundException();

		taskList.addTask(task);
		return taskDAO.persist(task);
	}
	
	@PUT
	@Path("/{id}/task/{task_id}")
	public Task updateTaskOfTasklist(@PathParam("id") long listId, @PathParam("task_id") long taskId, Task task) {
		Task existingTask = taskDAO.findTaskByListIdAndTaskId(listId, taskId);
		if (existingTask == null)
			throw new NotFoundException();

		task.setTaskList(existingTask.getTaskList());
		task.setId(taskId);

		return taskDAO.merge(task);
	}

	@DELETE
	@Path("/{id}")
	public Tasklist deleteTasklist(@PathParam("id") long listId) {
		Tasklist tasklist = taskListDAO.find(listId);
		if (tasklist == null)
			throw new NotFoundException();

		return taskListDAO.remove(tasklist);
	}

	@DELETE
	@Path("/{id}/done_tasks")
	public List<Task> deleteTasklistDoneTasks(@PathParam("id") long listId) {
		Tasklist tasklist = taskListDAO.find(listId);
		if (tasklist == null)
			throw new NotFoundException();

		ArrayList<Task> tasksToRemove = new ArrayList<>();		
		for (Task task : tasklist.getTasks()) {
			if (task.isDone())
				tasksToRemove.add(task);
		}
		
		tasklist.getTasks().removeAll(tasksToRemove);
		taskDAO.remove(tasksToRemove);

		return tasksToRemove;
	}
	
//
//	@GET
//	@Path("/{id}/task/{task_id}")
//	public Task readTask(@PathParam("id") long listId, @PathParam("task_id") long taskId) {
//		Task task = taskDAO.findTaskByListIdAndTaskId(listId, taskId);
//
//		if (task == null)
//			throw new NotFoundException();
//
//		return task;
//	}

	
//	@GET
//	@Path("/{id}")
//	public Tasklist readTasklist(@PathParam("id") long id) {
//		Tasklist tasklist = taskListDAO.find(id);
//		if (tasklist == null)
//			throw new NotFoundException();
//
//		return tasklist;
//	}
	

//	@GET
//	@Path("/{id}/task/")
//	public List<Task> readAllTasks(@PathParam("id") long id) {
//		List<Task> tasklist = taskListDAO.findAllTasksByListId(id);
//
//		if (tasklist == null)
//			throw new NotFoundException();
//
//		return tasklist;
//	}
	
	
//	@PUT
//	@Path("/{id}")
//	public Tasklist updateTasklist(@PathParam("id") long id, Tasklist tasklist) {
//		if (!taskListDAO.exists(id))
//			throw new NotFoundException();
//
//		tasklist.setId(id);
//		return taskListDAO.merge(tasklist);
//	}
	

//	@DELETE
//	@Path("/{id}/task/{task_id}")
//	public Task deleteTask(@PathParam("id") long listId, @PathParam("task_id") long taskId) {
//		Task task = taskDAO.findTaskByListIdAndTaskId(listId, taskId);
//		if (task == null)
//			throw new NotFoundException();
//
//		Tasklist taskList = task.getTaskList();
//		taskList.getTasks().remove(task);
//
//		return taskDAO.remove(task);
//	}
}
