package com.kosign.push.publics;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;


import com.kosign.push.apps.Application;
import com.kosign.push.devices.Device;
import com.kosign.push.notificationHistory.dto.ResponseHistoryDto;
import com.kosign.push.platformSetting.PlatformSetting;
import com.kosign.push.platforms.Platform;

import com.kosign.push.users.User;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.FileStorage;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.Response;

import com.kosign.push.utils.messages.APNS;
import com.kosign.push.utils.messages.ApplicationIdentifier;
import com.kosign.push.utils.messages.ApplicationResponse;
import com.kosign.push.utils.messages.ApplicationResponseById;
import com.kosign.push.utils.messages.DeviceClientRespose;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1")
public class BackendController extends SuperController{
   
   

    @GetMapping("/applications")
    public Object getYourApplication(@RequestParam(required = false,defaultValue = "") String appName) {
        UserDetail userDetail = GlobalMethod.getUserCredential();
       
        if(userDetail == null ){
            return Response.getResponseBody(KeyConf.Message.FAIL, "User Id Not Found", false);
        }else{ 
            List<ApplicationResponse> applications;
            
                applications  = appService.getActiveAppsByUserIdAndName(userDetail.getId(),appName);
          
            return Response.getResponseBody(KeyConf.Message.SUCCESS, applications, true);
        }
        
    }
    @GetMapping("/applications/{id}")
    public Object getYourApplicationByID(@PathVariable("id") String id ) {
        UserDetail userDetail = GlobalMethod.getUserCredential();
       
        if(userDetail == null ){
            return Response.getResponseBody(KeyConf.Message.FAIL, "User Id Not Found", false);
        }else{ 
            List<ApplicationResponseById> applications = appService.getActiveAppsByAppId(userDetail.getId(),id);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, applications, true);
        }
        
    }
    

    @PostMapping("/applications")
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


    @PutMapping("/applications")
    public Object updateName(@RequestBody ApplicationIdentifier application) throws Exception{


        Boolean update = appService.updateApplication(application.getId(),application.getName());


        return update ?

                Response.getResponseBody(KeyConf.Message.SUCCESS," {} ",true) :

                Response.getResponseBody(KeyConf.Message.FAIL, " {} ", false);


    }

    @DeleteMapping("/applications")
    public Object disabled(@RequestBody ApplicationIdentifier application){


            Boolean update = appService.disableApplication(application.getId());


        return update ?

                Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) :

                Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);


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
    @PostMapping("/platforms/setting/apns")
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
    @PutMapping("/platforms/setting/apns")
    public Object updateApns(String appId,MultipartFile p8file,String fileKey,String teamId,String bundleId) throws Exception {

            String file = FileStorage.uploadFile(p8file);
            Boolean updateStatus = platformSettingService.updateApns(appId, new APNS(file,teamId,fileKey,bundleId));
            return updateStatus ?
                  ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, "{}", true)) :
                  Response.getResponseBody(KeyConf.Message.FAIL,  "{}", false);

    }

    @DeleteMapping("/platforms/setting/apns")
    public Object deleteApnsConfiguration (String appId) {
        try {
            return platformSettingService.removeApnsConfiguration(appId) ? 
                    Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) : 
                    Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);

        } catch (Exception e) {
            return Response.getResponseBody(KeyConf.Message.FAIL,e.getLocalizedMessage().toUpperCase(), false);
        }
       
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
    @PostMapping("platforms/setting/fcm")
    public Object saveFcm(String appId,String platformId,String authKey){
        try {
            PlatformSetting platformSetting= platformSettingService.saveFcm(appId,platformId, authKey);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, platformSetting, true);
        } catch (Exception e) {
          
              return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }
        
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("platforms/setting/fcm")
    public Object updateFcm(String appId,String authKey){
        try {
            Boolean updateStatus = platformSettingService.updateFcm(appId, authKey);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, "{}", true);
        } catch (Exception e) {

            return Response.getResponseBody(KeyConf.Message.FAIL,  e.getMessage(), false);
        }

    }

    @Transactional(rollbackOn = Exception.class)
    @DeleteMapping("/platforms/setting/fcm")
    public Object deleteFcmConfiguration (String appId,String platform) {
        try {
            return platformSettingService.removeFcmConfiguration(appId,platform) ? 
                    Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) : 
                    Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);

        } catch (Exception e) {
            return  Response.getResponseBody(KeyConf.Message.FAIL,e.getLocalizedMessage().toUpperCase(), false);
        }
    }
  
    
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping("/platforms")
    public Object get(){
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformService.getActivePlatform(), true))  ;
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping("/platforms")
    public Object save (@RequestBody Platform platform) { 
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformService.insert(platform), true))  ;
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping("/platforms")
    public Object update (@RequestBody Platform platform) { 
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS,  platformService.update(platform),true))  ;
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @DeleteMapping("/platforms")
    public Object remove (@RequestBody Platform platform) { 
       
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS,  platformService.remove(platform),true))  ;
    } 
    
    @GetMapping("/push/history")
    public Object getHistory(String startDate,String endDate,String msgTitle) {

        List<ResponseHistoryDto> listHis = historyService.getAllHistory(startDate, endDate, msgTitle);
        
        return Response.getResponseBody(KeyConf.Message.SUCCESS,listHis , true);
    }
    
    @GetMapping("/push/history/{id}")
    public Object displayHistory(@PathVariable("id")Integer id){
     ResponseHistoryDto notiHisto= historyService.getPushNotificationHistoryById(id);
     return Response.getResponseBody(KeyConf.Message.SUCCESS,notiHisto, true);
    }
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PostMapping("/create/request")
    public Object  create(String username,String password) throws Exception{
        
        return Response.getResponseBody(KeyConf.Message.SUCCESS ,userService.saveUserToRequestStatus(username, password),true);
    }
   
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping(value="/{userId}/approval")
    public Object approval(@PathVariable("userId") String userId) throws Exception{
        return userService.approveUser(userId);
    }
    // @Transactional(rollbackOn = Exception.class)
    // @GetMapping("/devices")
    // public Object getDevice(String appId){
    //         List<Device> devices = deviceService.getActiveDeviceByAppId(appId);
    //         return Response.getResponseBody(KeyConf.Message.SUCCESS,devices,true);
    // }

    @ApiOperation("Get Device detail")
    @GetMapping("/devices")
    public Object getDeviceDetail(String appId,String startDate, String endDate, String push_id, String modelName, String plat_code, String os_version ) {

        List<DeviceClientRespose> listDeviceClients = deviceService.getAllDevicesClient(appId,startDate, endDate, push_id, modelName, plat_code, os_version);
        
        return Response.getResponseBody(KeyConf.Message.SUCCESS,listDeviceClients , true);
    }
}