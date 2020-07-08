package com.kosign.push.apps.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosign.push.platformSetting.dto.ResponsePlatformSetting;

import lombok.Data;
@Data
public class ResponseListAppById extends ResponseListApp {
  
    @JsonProperty("PLAT_REC")
    protected List<ResponsePlatformSetting> platRec;
    
}