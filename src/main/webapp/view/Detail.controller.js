/*global location */
sap.ui.define([
	       	"sap/ui/core/mvc/Controller",
			"sap/ui/model/json/JSONModel",
			"sap/ui/core/UIComponent",
			"sap/ui/model/Filter",
			"sap/ui/model/FilterOperator",
			"sap/m/Dialog",
			"sap/m/List",
			"sap/m/Button",
			"sap/m/Input",
			"../utils/Rest"
		],

		function (Controller, JSONModel, UIComponent, Filter, FilterOperator, Dialog, List, Button, Input, Rest) {
			"use strict";

			return Controller.extend("com.sap.todo.view.Detail", {

				onInit : function() {
					var oRouter = this.getOwnerComponent().getRouter();
					oRouter.getRoute("object").attachPatternMatched(this.onRouteMatched, this);
				},

				onRouteMatched : function(oEvent) {
					this._iSelectedTasklistId = oEvent.getParameter("arguments").id;
					this.getView().getModel("global").setProperty("/selectedTasklistId", parseInt(this._iSelectedTasklistId));					
					this.getView().bindElement("/lists/" + this.arrayIndexOfTaskListByID(this._iSelectedTasklistId));
				},
				
				arrayIndexOfTaskListByID : function(iTaskListId) {
					var aLists = this.getView().getModel().getProperty("/lists");
					for (var i = 0; i < aLists.length; i++) {
						if (aLists[i].id == iTaskListId) {
							return i;
						}
					}
				},

				handleTaskCheckboxPress : function(oEvent) {
					var self = this;
					
					var oListItem = oEvent.getParameter("listItem");
					var sPath = oListItem.getBindingContext().getPath();
					var task = this.getView().getModel().getProperty(sPath);
					
					Rest.put("./api/tasklist/" + this._iSelectedTasklistId + "/task/" + task.id, task, function() {
						self.getOwnerComponent().refreshData();
					});
				},
				
				handleToolbarAddTaskPress : function(oEvent) {
					var self = this;
					if (!this._addTaskDialog) {
						this._addTaskDialog = sap.ui.xmlfragment("com.sap.todo.fragment.AddTaskDialog", this);
						this.getView().addDependent(this._addTaskDialog);
					}
					this._addTaskDialog.open();
				},		
				
				handleAddTaskDialogOkPress: function() {
					var self = this;
					var sDescription = this.getView().getModel().getProperty("/newTaskDescription");

					Rest.post("./api/tasklist/" + this._iSelectedTasklistId + "/task/", { description: sDescription }, function(err, data) {
						self.getOwnerComponent().refreshData();
						self._addTaskDialog.close();
					});
				},
				
				handleAddTaskDialogCancelPress: function() {
					this._addTaskDialog.close();
				},

				handleToolbarActionPress : function(oEvent) {
					var oButton = oEvent.getSource();
					if (!this._actionSheet) {
						this._actionSheet = sap.ui.xmlfragment("com.sap.todo.fragment.ActionSheet", this);
						this.getView().addDependent(this._actionSheet);
					}
					this._actionSheet.openBy(oButton);
				},

				handleActionSheetDeleteDoneTasksPress: function(oEvent) {
					var self = this;
					Rest.del("./api/tasklist/" + this._iSelectedTasklistId + "/done_tasks/", function(err, data) {
						self.getOwnerComponent().refreshData();
					});
				},
				
				handleActionSheetDeleteListPress : function(oEvent) {
					var self = this;
					Rest.del("./api/tasklist/" + this._iSelectedTasklistId, function() {
						self.getOwnerComponent().refreshData();
						
						var iFirstListId = self.getView().getModel().getProperty("/lists/0/id");
						if (iFirstListId)
							self.getOwnerComponent().getRouter().navTo("object", {
								id : iFirstListId
							}, true);
					});
				}		
		});
	}
);
