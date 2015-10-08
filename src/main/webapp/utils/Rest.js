/*global location */
sap.ui.define([], function() {
	"use strict";

	return {
		// asynchronous requests
		get : function(url, cb) {
			_request("GET", true, null, url, cb);
		},
		post : function(url, data, cb) {
			_request("POST", true, data, url, cb);
		},
		put : function(url, data, cb) {
			_request("PUT", true, data, url, cb);
		},
		del : function(url, cb) {
			_request("DELETE", true, null, url, cb);
		},

		// synchronous request
		getSync : function(url, cb) {
			_request("GET", false, null, url, cb);
		},
		postSync : function(url, data, cb) {
			_request("POST", false, data, url, cb);
		},
		putSync : function(url, data, cb) {
			_request("PUT", false, data, url, cb);
		},
		delSync : function(url, cb) {
			_request("DELETE", false, null, url, cb);
		}
	}
	
	function _request(method, async, data, url, cb) {
		var options = {
			contentType : "application/json",
			dataType : "json",
			method : method,
			async : async,
			url : url,
			success : function(data, textStatus, jqXHR) {
				if (cb) {
					cb(null, data);
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				if (cb) {
					cb(errorThrown, null);
				}
			}
		};

		if (data) {
			options.data = JSON.stringify(data);
		}

		jQuery.ajax(options);
	}
});
