package com.kosign.push.publics;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.kosign.push.apps.AppService;
import com.kosign.push.apps.Application;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.mybatis.MybatisService;
import com.kosign.push.platformSetting.PlatformSetting;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.platforms.Platform;
import com.kosign.push.platforms.PlatformService;
import com.kosign.push.topics.TopicService;
import com.kosign.push.users.User;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.FileStorage;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.Response;

import com.kosign.push.utils.messages.APNS;
import com.kosign.push.utils.messages.ApplicationResponse;
import com.kosign.push.utils.messages.ApplicationResponseById;
import com.kosign.push.utils.messages.DeviceClientRespose;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
/**
 *  All Methods within this class is secured by Aspect class
 *  1. appId ownership
 * */
@Api(tags = "KOSIGN Push Backend API")
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/public")
public class PublicController extends SuperController{
    @Autowired
    private DeviceService deviceService;

    @GetMapping("/applications")
    public Object getYourApplication() {
        UserDetail userDetail = GlobalMethod.getUserCredential();
       
        if(userDetail == null ){
            return Response.getResponseBody(KeyConf.Message.FAIL, "User Id Not Found", false);
        }else{ 
            List<ApplicationResponse> applications = mybatisService.getActiveAppsByUserId(userDetail.getId());
            return Response.getResponseBody(KeyConf.Message.SUCCESS, applications, true);
        }
        
    }
    @GetMapping("/applications/{id}")
    public Object getYourApplicationByID(@PathVariable("id") String id ) {
        UserDetail userDetail = GlobalMethod.getUserCredential();
       
        if(userDetail == null ){
            return Response.getResponseBody(KeyConf.Message.FAIL, "User Id Not Found", false);
        }else{ 
            List<ApplicationResponseById> applications = mybatisService.getActiveAppsByAppId(userDetail.getId(),id);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, applications, true);
        }
        
    }
    

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


    @PutMapping("/applications/update")
    public Object updateName(@RequestBody Application application) throws Exception{


        Boolean update = appService.updateApplication(application.getId(),application.getName());


        return update ?

                Response.getResponseBody(KeyConf.Message.SUCCESS," {} ",true) :

                Response.getResponseBody(KeyConf.Message.FAIL, " {} ", false);


    }

    @PutMapping("/applications/delete")
    public Object disabled(@RequestBody Application application){


            Boolean update = appService.disableApplication(application.getId());


        return update ?

                Response.getResponseBody(KeyConf.Message.SUCCESS," {} ",true) :

                Response.getResponseBody(KeyConf.Message.FAIL, " {} ", false);


    }

    @GetMapping("/platforms/setting")
    public Object getAllPlatformSetting(String appId) throws Exception{

        try {

            List<PlatformSetting> platformSettings = platformSettingService.getActivePlatformsConfiguredByAppId(appId);
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformSettings, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }
/**
 *
 * FCM
 * */
    @GetMapping("/platforms/setting/apns")
    public Object getApns(String appId) throws Exception{

        try {

            PlatformSetting platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,KeyConf.PlatForm.IOS);
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformSetting, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @PostMapping("platforms/setting/apns/create")
    public Object saveApns(String appId,MultipartFile p8file,String fileKey,String teamId,String bundleId) throws Exception{
            
            try {
               
                    String file = FileStorage.uploadFile(p8file);
                    PlatformSetting platformSetting= platformSettingService.saveApns(appId, file, fileKey, teamId, bundleId);
                    return Response.getResponseBody(KeyConf.Message.SUCCESS, platformSetting, true);
                
               
            } catch (Exception e) {
                e.printStackTrace();
                return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
            }
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("/platforms/setting/apns/update")
    public Object updateApns(String appId,MultipartFile p8file,String fileKey,String teamId,String bundleId) throws Exception {

            String file = FileStorage.uploadFile(p8file);
            Boolean updateStatus = platformSettingService.updateApns(appId, new APNS(file,teamId,fileKey,bundleId));
            return updateStatus ?
                  ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, "{}", true)) :
                  Response.getResponseBody(KeyConf.Message.FAIL,  "{}", false);

    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("/platforms/setting/apns/delete")
    public Object deleteApnsConfiguration (String appId) {
        return null;
    }
/**
 *
 * FCM
 * */

    @GetMapping("/platforms/setting/fcm")
    public Object getFcm(String appId) throws Exception{

        try {

            PlatformSetting platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,KeyConf.PlatForm.ANDROID);
            PlatformSetting platformSettingWeb = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,KeyConf.PlatForm.WEB);
            
            List<PlatformSetting> platformList = new ArrayList<>();
            platformList.add(platformSetting);
            platformList.add(platformSettingWeb);
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformList, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }

    // @PreAuthorize("@appService.isOwner( authentication.getId(), #appId )")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("platforms/setting/fcm/create")
    public Object saveFcm(String appId,String platformId,String authKey){
        try {
            PlatformSetting platformSetting= platformSettingService.saveFcm(appId,platformId, authKey);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, platformSetting, true);
        } catch (Exception e) {
          
              return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }
        
    }

//    @PostMapping("platforms/fcm/create")
    public Object saveFcmTest(String appId,String authKey){
        return null;

    }
    @Transactional(rollbackOn = Exception.class)
    @PutMapping("platforms/setting/fcm/update")
    public Object updateFcm(String appId,String authKey){
        try {
            Boolean updateStatus = platformSettingService.updateFcm(appId, authKey);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, "{}", true);
        } catch (Exception e) {

            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }

    }


    @Transactional(rollbackOn = Exception.class)
    @GetMapping("/devices")
    public Object getDevice(String appId){
            List<Device> devices = deviceService.getActiveDeviceByAppId(appId);
            return Response.getResponseBody(KeyConf.Message.SUCCESS,devices,true);
    }

    
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping("/platforms")
    public Object get(){
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformService.getActivePlatform(), true))  ;
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping("/platforms/save")
    public Object save (@RequestBody Platform platform) { 
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformService.insert(platform), true))  ;
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping("/platforms/update")
    public Object update (@RequestBody Platform platform) { 
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS,  platformService.update(platform),true))  ;
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping("/platforms/remove")
    public Object remove (@RequestBody Platform platform) { 
       
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS,  platformService.remove(platform),true))  ;
    }
     
    @PostMapping("/devices/client")
    public Object create(String startDate, String endDate, String push_id, String modelName, String plat_code, String os_version ) {

        List<DeviceClientRespose> listDeviceClients = deviceService.getAllDevicesClient(startDate, endDate, push_id, modelName, plat_code, os_version);
        
        return Response.getResponseBody(KeyConf.Message.SUCCESS,listDeviceClients , true);
    }
}