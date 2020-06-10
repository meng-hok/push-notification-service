package com.kosign.push.commons;

public abstract class GenericRestClient {

	private String apiUrl;
	private String apiSecrKey;
	
	public String getApiUrl() {
		return apiUrl;
	}

	public void setApiUrl(String apiUrl) {
		this.apiUrl = apiUrl;
	}

	public String getApiSecrKey() {
		return apiSecrKey;
	}

	public void setApiSecrKey(String apiSecrKey) {
		this.apiSecrKey = apiSecrKey;
	}
}
