package com.kosign.push.devices.dto;

import lombok.Data;

@Data
public class RequestDevice {
    
    public String appId;
    public String startDate; 
    public String endDate; 
    public String push_id; 
    public String modelName; 
    public String plat_code; 
    public String os_version;
}