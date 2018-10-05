package com.sliit.synchronizertokenpattern.model;

import java.util.HashMap;

/**
 * @author Saranki
 *
 */
public class ServerStore {

	public HashMap<String, String> sessions;

	public static volatile ServerStore serverStore;

	/**
	 * 
	 */
	private ServerStore() {
		sessions = new HashMap<>();
	}

	public static ServerStore getUserSessionDetails() {
		if (serverStore == null) {
			synchronized (ServerStore.class) {
				if (serverStore == null) {
					serverStore = new ServerStore();
				}
			}
		}

		return serverStore;
	}

	public void storeCSRFToken(String session, String token) {
		if (sessions.get(session) != null) {
			// If token has been expired change the token for the session id
			sessions.replace(session, token);
		} else {
			sessions.put(session, token);
		}

	}
	
	public String retrieveCSRFToken(String sessionId){
		return sessions.get(sessionId);
		
	}

}
