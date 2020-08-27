package com.kosign.push.platformSetting.dto;

import javax.validation.constraints.NotEmpty;

import com.kosign.push.utils.aspects.ApplicationAspect;

import lombok.Data;
/**
 * 
 * Super class which many class inherited from
 */
@Data
public class FCMIdentifier extends ApplicationAspect {
        
   // public String appId;
   @NotEmpty
   public String authorizedKey;

//   public String bulkId;
}