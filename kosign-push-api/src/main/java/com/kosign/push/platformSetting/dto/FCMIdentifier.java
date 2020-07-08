package com.kosign.push.platformSetting.dto;

import lombok.Data;
/**
 * 
 * Super class which many class inherited from
 */
@Data
public class FCMIdentifier {
        
   public String appId;
   public String authorizedKey;
}