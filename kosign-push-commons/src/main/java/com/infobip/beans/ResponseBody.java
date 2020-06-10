package com.infobip.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class ResponseBody {
	
	public ResponseBody() {
		
	}
	
	@JsonIgnore
	private String responseData;
	
	@JsonIgnore
	private boolean success;

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}
	
	
	public abstract boolean isSuccess();

}
