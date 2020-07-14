package com.kosign.push.platformSetting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kosign.push.utils.aspects.ApplicationAspect;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
@Data
public class RequestCreateApns extends ApplicationAspect{
    

    public String team_id;
  
    public String file_key;

 
    public String bundle_id;

 
}