package com.infobip.cmo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.infobip.beans.Message;

import java.util.List;


public class SingleTextualMessageOut extends InfobipOut {

	@JsonProperty("messages")
	private List<Message> messages;

	public List<Message> getMessages() {
		return messages;
	}

	public void setMessages(List<Message> messages) {
		this.messages = messages;
	}
	
}
