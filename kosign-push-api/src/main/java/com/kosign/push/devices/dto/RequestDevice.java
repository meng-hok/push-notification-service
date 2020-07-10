package com.kosign.push.devices.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class RequestDevice {
   
  
    public String push_id; 
    public String modelName; 
    public String plat_code; 
    public String os_version;

}