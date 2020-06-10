package com.infobip.cmo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TextualMessageIn extends InfobipIn {
	
	@JsonIgnore
	private String weumsId;
	
	@JsonIgnore
	private String apiSecret;
	
	@JsonProperty("from")
	private String from;
	
	@JsonProperty("to")
	private String to;
	
	@JsonProperty("text")
	private String text;
}
