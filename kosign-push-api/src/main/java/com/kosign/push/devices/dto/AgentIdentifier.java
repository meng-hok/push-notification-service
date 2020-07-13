package com.kosign.push.devices.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;

@Data
public class AgentIdentifier extends ApplicationAspect{
   
    public String device_id;


    public String platform_id;

    public String token;
}