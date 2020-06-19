package com.kosign.push.publics;

import java.util.List;

import javax.transaction.Transactional;

import com.kosign.push.apps.AppService;
import com.kosign.push.apps.Application;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.platformSetting.PlatformSetting;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.platforms.Platform;
import com.kosign.push.users.User;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.FileStorage;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.Response;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
/**
 *  All Methods within this class is secured by Aspect class
 *  1. appId ownership
 * */
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/public")
public class PublicController {
   
    @Autowired
    private AppService appService;
    @Autowired
    private PlatformSettingService platformSettingService;
    @Autowired
    private DeviceService deviceService;
    
    @GetMapping("/applications")
    public Object getYourApplication() {
        UserDetail userDetail = GlobalMethod.getUserCredential();
       
        if(userDetail == null ){
            return Response.getResponseBody(KeyConf.Message.FAIL, "User Id Not Found", false);
        }else{ 
            List<Application> applications = appService.getActiveAppsByUserId(userDetail.getId());
            return Response.getResponseBody(KeyConf.Message.SUCCESS, applications, true);
        }
        
    }
    
    @ResponseBody
    @PostMapping("/applications/create")
    public Object create(String name){
        try {
            Application app = new Application();
            app.setName(name);
            app.setUser(new User(GlobalMethod.getUserCredential().getId()));
            return Response.getResponseBody(KeyConf.Message.SUCCESS, appService.save(app), true);
        } catch (Exception e) {
            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }
      
    }

    @Transactional(rollbackOn = Exception.class)
    @PostMapping("platforms/create/apns")
    public Object saveApns(String appId,MultipartFile p8file,String fileKey,String teamId,String bundleId) throws Exception{
            
            try {
               
                    String file = FileStorage.uploadFile(p8file);
                    PlatformSetting platformSetting= platformSettingService.saveApns(appId, file, fileKey, teamId, bundleId);
                    return Response.getResponseBody(KeyConf.Message.SUCCESS, platformSetting, true);
                
               
            } catch (Exception e) {
                return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
            }
    }

    // @PreAuthorize("@appService.isOwner( authentication.getId(), #appId )")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("platforms/create/fcm")
    public Object saveFcm(String appId,String authKey){
        try {
            PlatformSetting platformSetting= platformSettingService.saveFcm(appId, authKey);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, platformSetting, true);
        } catch (Exception e) {
          
              return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }
        
    }

   
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("/devices/create")
    public Object save(String appId,String deviceId,String userId,String platformId,String token){
        try {
            Device device;
            if(deviceId != null ){
                device = new Device(deviceId,token,new Application(appId),new Platform(platformId));
            }else if(userId != null ){
                device = new Device(token,new Application(appId),new Platform(platformId),userId);
            }else{
                throw new Exception("Created Fail");
            }
           
            return Response.getResponseBody(KeyConf.Message.SUCCESS,  deviceService.saveDevice(device), true);
        } catch (Exception e) {
            
            System.out.println(e.getMessage());
            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }

    }
    

}