/*global location */
sap.ui.define([ "sap/ui/core/UIComponent", "sap/ui/Device",
		"sap/ui/model/json/JSONModel", "./utils/Rest" ], function(UIComponent,
		Device, JSONModel, Rest) {
	"use strict";

	return UIComponent.extend("com.sap.todo.Component", {

		metadata : {
			manifest : "json"
		},

		init : function() {
			// call the base component's init function and create the App view
			UIComponent.prototype.init.apply(this, arguments);

			// set the device model
			this.setModel(new JSONModel(Device), "device");

			// set possible task list logos
			this.setModel(new JSONModel({
				icons : this.iconPool
			}), "global");

			// fetch task list from backend
			this.refreshData();

			// create the views based on the url/hash
			this.getRouter().initialize();
		},

		refreshData : function() {
			var self = this;
			Rest.getSync("./api/tasklist/", function(err, data) {
				if (!err)
					self.setModel(new JSONModel({
						lists : data
					}));
			});
		},

		iconPool : [ {
			url : ""
		}, {
			url : "sap-icon://cart"
		}, {
			url : "sap-icon://competitor"
		}, {
			url : "sap-icon://course-book"
		}, {
			url : "sap-icon://create-leave-request"
		}, {
			url : "sap-icon://electrocardiogram"
		}, {
			url : "sap-icon://employee"
		}, {
			url : "sap-icon://factory"
		}, {
			url : "sap-icon://favorite"
		}, {
			url : "sap-icon://flag"
		}, {
			url : "sap-icon://globe"
		}, {
			url : "sap-icon://group"
		}, {
			url : "sap-icon://history"
		}, {
			url : "sap-icon://home"
		}, {
			url : "sap-icon://inbox"
		}, {
			url : "sap-icon://lateness"
		}, {
			url : "sap-icon://meal"
		}, {
			url : "sap-icon://notification"
		}, {
			url : "sap-icon://physical-activity"
		}, {
			url : "sap-icon://visits"
		}, {
			url : "sap-icon://wallet"
		} ]
	});
});
