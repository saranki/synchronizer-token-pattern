package com.sliit.synchronizertokenpattern.model;

public class User {

	private String username;
	private String password;
	private String sessionId;
	private String token;

	// password = admin123

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", sessionId=" + sessionId + ", token=" + token
				+ "]";
	}

}
