package com.kosign.push.apps.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosign.push.utils.messages.PlatformSettingRespone;

import lombok.Data;
@Data
public class ApplicationResponseById extends ApplicationResponse {
  
    @JsonProperty("PLAT_REC")
    protected List<PlatformSettingRespone> platRec;
    
}