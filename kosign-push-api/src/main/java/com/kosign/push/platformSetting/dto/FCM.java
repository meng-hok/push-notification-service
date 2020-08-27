package com.kosign.push.platformSetting.dto;

import lombok.Data;
import lombok.ToString;
@Data
@ToString
public class FCM extends FCMIdentifier {
   public String token;
   public String title;
   public String message;
   public String bulkId;
   public FCM(String authorizedKey, String token, String title, String message) {
       this.authorizedKey = authorizedKey;
       this.token = token;
       this.title = title;
       this.message = message;
   }

   public FCM(){}
}