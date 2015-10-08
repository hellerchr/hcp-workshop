/*global location */
sap.ui.define([
       	"sap/ui/core/mvc/Controller",
		"sap/ui/model/json/JSONModel",
		"sap/ui/core/UIComponent",
		"../utils/Rest"
	], function (Controller, JSONModel, UIComponent, Rest) {
		"use strict";

		return Controller.extend("com.sap.todo.view.Master", {
			
			onInit : function() {
				this.getOwnerComponent().getRouter().getRoute("master").attachPatternMatched(this.onRouteMatched, this);
			},
		
			onRouteMatched : function(oEvent) {
				var iFirstListId = this.getView().getModel().getProperty("/lists/0/id");
				if (iFirstListId)
					this.showDetailViewForListId(iFirstListId);
			},
			
			showDetailViewForListId : function(id) {
				this.getOwnerComponent().getRouter().navTo("object", {
					id : id
				}, true);
			},
						
			handleListSelectionChange: function(oEvent) {
				var oItem = oEvent.getParameter("listItem");
				var iId = oItem.getBindingContext().getProperty("id");				
				this.showDetailViewForListId(iId);
			},
			
			handleAddTasklistIconSelectionChange: function(oEvent) {
				this._selectedIcon = oEvent.getParameter("listItem").getIcon();
			},
			
			handleToolbarAddTasklistPress : function(oEvent) {
				var self = this;
				if (!this._addTasklistDialog) {
					this._addTasklistDialog = sap.ui.xmlfragment("com.sap.todo.fragment.AddTasklistDialog", this);
					this.getView().addDependent(this._addTasklistDialog);
				}
				this._addTasklistDialog.open();
			},
			
			handleAddTasklistDialogOkPress: function() {
				var self = this;
				var sName = this.getView().getModel().getProperty("/newTasklistName");
				var sIcon = this._selectedIcon;
				delete self._selectedIcon;

				Rest.post("./api/tasklist/", { name: sName, icon: sIcon }, function(err, data) {
					if (!err) {
						self.getOwnerComponent().refreshData();
						self.showDetailViewForListId(data.id);
						self._addTasklistDialog.close();
					}
				});
			},
			
			handleAddTasklistDialogCancelPress: function() {
				this._addTasklistDialog.close();
			},
			
			formatOpenTaskCount : function(tasks) {
				return tasks.filter(function(task) { return !task.done }).length
			}			
		});
	}
);