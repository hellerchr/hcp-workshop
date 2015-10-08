package com.sap.todo.api;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.sap.security.um.user.PersistenceException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

@Path("/meta")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MetaAPI {

	@Inject
	UserProvider provider;

	private final static String[] CLOUD_ENV_VARS = { "HC_HOST", "HC_REGION", "HC_ACCOUNT", "HC_APPLICATION",
			"HC_APPLICATION_URL", "HC_LOCAL_HTTP_PORT", "HC_LANDSCAPE", "HC_PROCESS_ID", "HC_OP_HTTP_PROXY_HOST",
			"HC_OP_HTTP_PROXY_PORT" };

	@GET
	@Path("/cloud")
	public Map<String, String> getCloudMetadata() {
		HashMap<String, String> map = new HashMap<>();
		for (String var : CLOUD_ENV_VARS) {
			map.put(var, System.getenv(var));
		}
		return map;
	}

	@GET
	@Path("/user")
	public Map<String, Object> getUserInformation(@Context SecurityContext context) {
		HashMap<String, Object> map = new HashMap<>();
		try {
			User currentUser = provider.getCurrentUser();
			map.put("name", currentUser.getName());
			map.put("locale", currentUser.getLocale());
			map.put("groups", currentUser.getGroups());
			map.put("roles", currentUser.getRoles());
		} catch (PersistenceException e) {
			e.printStackTrace();
		}
		return map;
	}
}
