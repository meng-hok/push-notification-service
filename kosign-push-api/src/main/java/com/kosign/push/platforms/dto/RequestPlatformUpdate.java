package com.kosign.push.platforms.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RequestPlatformUpdate 
{
	@NotNull
	private String name = null;
	@NotNull
	private String icon = null;
	@NotNull
	private String code = null;
}
