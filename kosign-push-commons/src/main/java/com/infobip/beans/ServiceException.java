package com.infobip.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceException {
	
	@JsonProperty("messageId")
	private String messageId;
	
	@JsonProperty("text")
	private String text;
	
	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	
}
