package com.kosign.push.utils;

import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.platforms.PlatformEntity;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.enums.PlatformEnum;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class GlobalMethod {
    
   public static PlatformEntity getAndroid () {
      return new PlatformEntity(PlatformEnum.Platform.ANDROID);
   }
   public static PlatformEntity getIos () {
      return new PlatformEntity(PlatformEnum.Platform.IOS);
   }
   
   public static PlatformEntity getBrowser() {
      return new PlatformEntity(PlatformEnum.Platform.WEB);
   }

   public static UserDetail getUserCredential(){
      try {
         Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

         UserDetail userDetail = (UserDetail)authentication.getPrincipal();
         return userDetail;
      } catch (Exception e) {
         return null;
      }
   }

   public static List<String> convertDeviceListToTokenList(List<DeviceEntity> devices){
      List<String> tokens = new ArrayList<>();
      devices.forEach(device -> {
         tokens.add(device.getToken());
      });
      return tokens;
   }

}