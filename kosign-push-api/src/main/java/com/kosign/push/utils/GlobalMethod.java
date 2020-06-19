package com.kosign.push.utils;

import com.kosign.push.devices.Device;
import com.kosign.push.platforms.Platform;
import com.kosign.push.users.UserDetail;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class GlobalMethod {
    
   public static Platform getAndroid () {
      return new Platform(KeyConf.PlatForm.ANDROID);
   }
   public static Platform getIos () {
      return new Platform(KeyConf.PlatForm.IOS);
   }
   
   public static Platform getBrowser() {
      return new Platform(KeyConf.PlatForm.WEB);
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

   public static List<String> convertDeviceListToTokenList(List<Device> devices){
      List<String> tokens = new ArrayList<>();
      devices.forEach(device -> {
         tokens.add(device.getToken());
      });
      return tokens;
   }

}