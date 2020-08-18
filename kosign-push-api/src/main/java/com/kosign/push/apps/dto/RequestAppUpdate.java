package com.kosign.push.apps.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RequestAppUpdate 
{
	@NotEmpty
	private String name = null;
}
