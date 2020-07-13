package com.kosign.push.devices.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;

@Data
public class RequestAgent extends ApplicationAspect {
    public String title;
    public String message;
    public String device_id; 
   
}