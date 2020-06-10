package com.infobip.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {

	@JsonProperty("to")
	private String to;
	
	@JsonProperty("status")
	private MessageStatus status;
	
	@JsonProperty("smsCount")
	private int smsCount;
	
	@JsonProperty("messageId")
	private String messageId;
	
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public int getSmsCount() {
		return smsCount;
	}

	public void setSmsCount(int smsCount) {
		this.smsCount = smsCount;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	
}
