package com.kosign.push.devices.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class RequestAgent {
    @Size(max = 100)
    public String title;
    @Size(max = 255)
    public String message;
    @NotEmpty
    public String device_id; 
   
}