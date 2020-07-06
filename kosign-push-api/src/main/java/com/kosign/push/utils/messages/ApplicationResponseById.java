package com.kosign.push.utils.messages;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class ApplicationResponseById extends ApplicationResponse {
  
    @JsonProperty("PLAT_REC")
    protected List<PlatformSettingRespone> platRec;
    
}