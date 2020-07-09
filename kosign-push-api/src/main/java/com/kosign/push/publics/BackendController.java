package com.kosign.push.publics;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;


import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.devices.dto.ResponseDevice;
import com.kosign.push.notificationHistory.dto.ResponseHistoryDto;
import com.kosign.push.platformSetting.PlatformSettingEntity;
import com.kosign.push.platforms.PlatformEntity;

import com.kosign.push.users.UserDetail;
import com.kosign.push.users.UserEntity;
import com.kosign.push.utils.FileStorage;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.Response;

import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.dto.RequestCreateApns;
import com.kosign.push.platformSetting.dto.RequestCreateFcm;
import com.kosign.push.platformSetting.dto.RequestRemoveApns;
import com.kosign.push.platformSetting.dto.RequestRemoveFcm;
import com.kosign.push.platformSetting.dto.RequestUpdateFcm;
import com.kosign.push.apps.dto.RequestAppIdentifier;
import com.kosign.push.apps.dto.RequestCreateApp;
import com.kosign.push.apps.dto.RequestRemoveApp;
import com.kosign.push.apps.dto.ResponseListAppById;
import com.kosign.push.apps.dto.ResponseCommonApp;
import com.kosign.push.apps.dto.ResponseListApp;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import jdk.jfr.ContentType;
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
            List<ResponseListApp> applications;
            
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
            List<ResponseListAppById> applications = appService.getActiveAppsByAppId(userDetail.getId(),id);
            return Response.getResponseBody(KeyConf.Message.SUCCESS, applications, true);
        }
        
    }
    

    @PostMapping("/applications")
    public Object create(@RequestBody RequestCreateApp applicationCreateRequest){
        try {
            if(appService.getAppByNameAndUserId(applicationCreateRequest.getName()) != null) {

                throw new Exception("Application is Being Registered");
            }

            AppEntity app = new AppEntity();
            app.setName(applicationCreateRequest.getName());
            AppEntity responseApp =  appService.save(app);
            if(responseApp==null ){ 
                throw new Exception("Create Fail");
            }
            //  ResponseCommonApp responseCommonApp =  new ResponseCommonApp(responseApp);
             return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return  Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);
        }
      
    }


    @PutMapping("/applications")
    public Object updateName(@RequestBody RequestAppIdentifier application) throws Exception{


        Boolean update = appService.updateApplication(application.getId(),application.getName());


        return update ?

                Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) :

                Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);


    }

    @DeleteMapping("/applications")
    public Object disabled(@RequestBody RequestRemoveApp object){


            Boolean update = appService.disableApplication(object.getAppId());


        return update ?

                Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) :

                Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);


    }

//    @GetMapping("/platforms/setting")
    public Object getAllPlatformSetting(@RequestParam(required = true) String appId) throws Exception{

        try {

            List<PlatformSettingEntity> platformSettings = platformSettingService.getActivePlatformsConfiguredByAppId(appId);


            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformSettings, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }
/**
 *
 * FCM
 * */
//    @GetMapping("/platforms/setting/apns")
    public Object getApns(@RequestParam(required = true) String appId) throws Exception{

        try {

            PlatformSettingEntity platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,KeyConf.PlatForm.IOS);
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformSetting, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }

    @CrossOrigin
    @Transactional(rollbackOn = Exception.class)
    @PostMapping( value = "/platforms/setting/apns")
    public Object saveApns(@RequestPart(required = true)MultipartFile p8file , RequestCreateApns requestCreateApns  ) throws Exception{
            
            try {

                    if (p8file.isEmpty() ) {

                        return Response.getFailResponseNonDataBody(KeyConf.Message.P8FILENOTFOUND);
                    }
                    if(platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(requestCreateApns.appId,KeyConf.PlatForm.IOS) != null ) {
                        return Response.getFailResponseNonDataBody(KeyConf.Message.PLATFORMSETTINGREGISTERED);
                    }
                    String file = FileStorage.uploadFile(p8file);
                    PlatformSettingEntity platformSetting= platformSettingService.saveApns(requestCreateApns.appId, file, requestCreateApns.fileKey, requestCreateApns.teamId, requestCreateApns.bundleId);
                    return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
                
               
            } catch (Exception e) {
                e.printStackTrace();
                return Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);
            }
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("/platforms/setting/apns")
    public Object updateApns(@RequestPart(required = true)MultipartFile p8file , RequestCreateApns requestCreateApns   ) throws Exception {
            if (p8file.isEmpty() ) {

                return Response.getFailResponseNonDataBody(KeyConf.Message.P8FILENOTFOUND);
            }
            String file = FileStorage.uploadFile(p8file);
            Boolean updateStatus = platformSettingService.updateApns(requestCreateApns.appId, new APNS(file,requestCreateApns.teamId,requestCreateApns.fileKey,requestCreateApns.bundleId));
            return updateStatus ?
                  Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) :
                  Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);

    }

    @DeleteMapping("/platforms/setting/apns")
    public Object deleteApnsConfiguration (@RequestBody RequestRemoveApns requestRemoveApns) {
        try {
            return platformSettingService.removeApnsConfiguration(requestRemoveApns.appId) ? 
                    Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) : 
                    Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);

        } catch (Exception e) {
            return Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);

        }
       
    }
/**
 *
 * FCM
 * */
  
//   @GetMapping("/platforms/setting/fcm")
    public Object getFcm(String appId) throws Exception{

        try {

            PlatformSettingEntity platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,KeyConf.PlatForm.ANDROID);
            PlatformSettingEntity platformSettingWeb = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,KeyConf.PlatForm.WEB);
            
            List<PlatformSettingEntity> platformList = new ArrayList<>();
            if (platformSetting != null  )
                platformList.add(platformSetting);
            if (platformSettingWeb != null  )
                platformList.add(platformSettingWeb);
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformList, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }

    // @PreAuthorize("@appService.isOwner( authentication.getId(), #appId )")
    @ApiOperation(value = "To register platform setting",notes = "Code = 2 (Fcm android ) & 3 (Fcm Web) ")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("platforms/setting/fcm")
    public Object saveFcm(@RequestBody RequestCreateFcm requestFcm){
        try {
            if(KeyConf.PlatForm.ANDROID.equals(requestFcm.getPlatformId()) | KeyConf.PlatForm.WEB.equals(requestFcm.getPlatformId()) ) {
                PlatformSettingEntity platformSetting= platformSettingService.saveFcm(requestFcm.getAppId(),requestFcm.getPlatformId(),requestFcm.getAuthorizedKey());
                return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
            }

           return Response.getFailResponseNonDataBody(KeyConf.Message.INCORRECTPLATFORM);
        } catch (Exception e) {
          
              return  Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);
        }
        
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("platforms/setting/fcm")
    public Object updateFcm(@RequestBody RequestUpdateFcm requestFcm){
        try {
            if( KeyConf.PlatForm.ANDROID.equals(requestFcm.getPlatformId()) | KeyConf.PlatForm.WEB.equals(requestFcm.getPlatformId()) ) {

                Boolean updateStatus = platformSettingService.updateFcm(requestFcm.appId, requestFcm.platformId ,requestFcm.authorizedKey);
                return updateStatus ?
                        Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) :
                        Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);
            }
            return Response.getFailResponseNonDataBody(KeyConf.Message.INCORRECTPLATFORM);
        } catch (Exception e) {

            return Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);
        }

    }

    @Transactional(rollbackOn = Exception.class)
    @DeleteMapping("/platforms/setting/fcm")
    public Object deleteFcmConfiguration (@RequestBody RequestRemoveFcm requestFcm) {
        try {
            return platformSettingService.removeFcmConfiguration(requestFcm.appId,requestFcm.platformId) ? 
                    Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) : 
                    Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);

        } catch (Exception e) {
            return   Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);
        }
    }
  
    
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping("/platforms")
    public Object get(){
        return ResponseEntity.ok(Response.getResponseBody(KeyConf.Message.SUCCESS, platformService.getActivePlatform(), true))  ;
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping("/platforms")
    public Object save (@RequestBody PlatformEntity platform) { 
        PlatformEntity _platform =  platformService.insert(platform);
         return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping("/platforms")
    public Object update (@RequestBody PlatformEntity platform) { 

        PlatformEntity _platform =  platformService.update(platform);
        return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @DeleteMapping("/platforms")
    public Object remove (@RequestBody PlatformEntity platform) { 
        Boolean boo = platformService.remove(platform);
        return boo ?  Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS) : 
                       Response.getFailResponseNonDataBody(KeyConf.Message.FAIL);
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
        UserEntity user = userService.saveUserToRequestStatus(username, password);
        return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
    }
   
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping(value="/{userId}/approval")
    public Object approval(@PathVariable("userId") String userId) throws Exception{
         userService.approveUser(userId);
        return Response.getSuccessResponseNonDataBody(KeyConf.Message.SUCCESS);
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

        List<ResponseDevice> listDeviceClients = deviceService.getAllDevicesClient(appId,startDate, endDate, push_id, modelName, plat_code, os_version);
        
        return Response.getResponseBody(KeyConf.Message.SUCCESS,listDeviceClients , true);
    }
}