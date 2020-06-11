package com.kosign.push.publics;

import javax.transaction.Transactional;

import com.kosign.push.apps.AppService;
import com.kosign.push.apps.Application;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.platformSetting.PlatformSetting;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.platforms.Platform;
import com.kosign.push.utils.FileStorage;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@PreAuthorize("hasRole('ROLE_USER')")
@RestController
@RequestMapping("/api/public")
public class PublicRestController {
   
    @Autowired
    private AppService appService;
    @Autowired
    private PlatformSettingService platformSettingService;
    @Autowired
    private DeviceService deviceService;
    @ResponseBody
    @PostMapping("/application/create")
    public Object create(String name){
        try {
            Application app = new Application();
            app.setName(name);
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
    public Object save(String deviceId,String platformId,String appId,String token){
        try {
            Device device = new Device(deviceId,token,new Application(appId),new Platform(platformId));
            return Response.getResponseBody(KeyConf.Message.SUCCESS,  deviceService.saveDevice(device), true);
        } catch (Exception e) {
            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }

    }

}