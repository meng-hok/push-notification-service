package com.infobip.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RequestError {

	@JsonProperty("serviceException")
	private ServiceException serviceException;

	public ServiceException getServiceException() {
		return serviceException;
	}

	public void setServiceException(ServiceException serviceException) {
		this.serviceException = serviceException;
	}
	
}
