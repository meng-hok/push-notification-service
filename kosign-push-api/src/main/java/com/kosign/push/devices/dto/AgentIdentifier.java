package com.kosign.push.devices.dto;

import lombok.Data;

@Data
public class AgentIdentifier {
   
    public String device_id;
  
    public String app_id;


    public String platform_id;

    public String token;
}