package com.kosign.push.platformSetting.dto;

import lombok.Data;
import lombok.ToString;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.*;

@Data
@ToString
public class FCM extends FCMIdentifier {
   public String token;
   public String title;
   public String message;
   @JsonProperty("badge_count")
   public Integer badgeCount;
   public String image;
    public String bulkId;
    @JsonProperty("action")
    public Map actionType;
   public FCM(String authorizedKey, String token, String title, String message,Map actionType) {
       this.authorizedKey = authorizedKey;
       this.token = token;
       this.title = title;
       this.message = message;
       this.actionType=actionType;
   }
    public FCM(String authorizedKey, String token, String title, String message) {
        this.authorizedKey = authorizedKey;
        this.token = token;
        this.title = title;
        this.message = message;
    }

   public FCM(){}
}