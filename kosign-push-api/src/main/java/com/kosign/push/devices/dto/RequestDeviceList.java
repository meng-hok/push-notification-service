package com.kosign.push.devices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class RequestDeviceList 
{
	private String appId     = null;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String startDate = null;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String endDate   = null;
	private String modelName = null;
	private String osVersion = null;
	private String platCode  = null;
	private String pushId    = null;
	
}
