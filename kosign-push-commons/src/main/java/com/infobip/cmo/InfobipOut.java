package com.infobip.cmo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infobip.beans.RequestError;
import com.infobip.beans.ResponseBody;


public class InfobipOut extends ResponseBody {
	
	public InfobipOut() {
		
	}

	@JsonProperty("requestError")
	private RequestError requestError;
	
	public RequestError getRequestError() {
		return requestError;
	}

	public void setRequestError(RequestError requestError) {
		this.requestError = requestError;
	}

	@Override
	public boolean isSuccess() {
		return (this.requestError == null);
	}
	
}
