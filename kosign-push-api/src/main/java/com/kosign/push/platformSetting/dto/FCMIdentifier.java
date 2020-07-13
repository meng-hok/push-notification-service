package com.kosign.push.platformSetting.dto;

import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;
/**
 * 
 * Super class which many class inherited from
 */
@Data
public class FCMIdentifier extends ApplicationAspect {
        
   // public String appId;
   public String authorizedKey;
}