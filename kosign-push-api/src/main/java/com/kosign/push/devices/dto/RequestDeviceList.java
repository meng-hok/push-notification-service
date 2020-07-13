package com.kosign.push.devices.dto;

import lombok.Data;

@Data
public class RequestDeviceList 
{
	private String appId     = null;
	private String startDate = null;
	private String endDate   = null;
	private String modelName = null;
	private String osVersion = null;
	private String platCode  = null;
	private String pushId    = null;
	
}
