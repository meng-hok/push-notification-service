package com.kosign.push.devices.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RequestAgent {
    public String title;
    public String message;
    @NotEmpty
    public String device_id; 
   
}