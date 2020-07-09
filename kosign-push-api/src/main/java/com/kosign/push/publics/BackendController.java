package com.kosign.push.publics;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.dto.RequestDevice;
import com.kosign.push.devices.dto.ResponseDevice;
import com.kosign.push.notificationHistory.dto.ResponseHistoryDto;
import com.kosign.push.platformSetting.PlatformSettingEntity;
import com.kosign.push.platforms.PlatformEntity;

import com.kosign.push.users.UserDetail;
import com.kosign.push.users.UserEntity;
import com.kosign.push.utils.FileStorage;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.Response;
import com.kosign.push.utils.enums.PlatformEnum;
import com.kosign.push.utils.enums.ResponseEnum;
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
import com.kosign.push.apps.dto.ResponseListApp;

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
            return Response.getResponseBody(ResponseEnum.Message.FAIL, "User Id Not Found", false);
        }else{ 
            List<ResponseListApp> applications;
            
                applications  = appService.getActiveAppsByUserIdAndName(userDetail.getId(),appName);
          
            return Response.getResponseBody(ResponseEnum.Message.SUCCESS, applications, true);
        }
        
    }
    @GetMapping("/applications/{id}")
    public Object getYourApplicationByID(@PathVariable("id") String id ) {
        UserDetail userDetail = GlobalMethod.getUserCredential();
       
        if(userDetail == null ){
            return Response.getResponseBody(ResponseEnum.Message.FAIL, "User Id Not Found", false);
        }else{ 
            List<ResponseListAppById> applications = appService.getActiveAppsByAppId(userDetail.getId(),id);
            return Response.getResponseBody(ResponseEnum.Message.SUCCESS, applications, true);
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
             return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return  Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        }
      
    }


    @PutMapping("/applications")
    public Object updateName(@RequestBody RequestAppIdentifier application) throws Exception{


        Boolean update = appService.updateApplication(application.getId(),application.getName());


        return update ?

                Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) :

                Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);


    }

    @DeleteMapping("/applications")
    public Object disabled(@RequestBody RequestRemoveApp object){


            Boolean update = appService.disableApplication(object.getAppId());


        return update ?

                Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) :

                Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);


    }

//    @GetMapping("/platforms/setting")
    public Object getAllPlatformSetting(@RequestParam(required = true) String appId) throws Exception{

        try {

            List<PlatformSettingEntity> platformSettings = platformSettingService.getActivePlatformsConfiguredByAppId(appId);


            return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.SUCCESS, platformSettings, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }
/**
 *
 * FCM
 * */
//    @GetMapping("/platforms/setting/apns")
    public Object getApns(@RequestParam(required = true) String appId) throws Exception{

        try {

            PlatformSettingEntity platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.IOS);
            return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.SUCCESS, platformSetting, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }

    @CrossOrigin
    @Transactional(rollbackOn = Exception.class)
    @PostMapping( value = "/platforms/setting/apns")
    public Object saveApns(@RequestPart(required = true)MultipartFile p8file , RequestCreateApns requestCreateApns  ) throws Exception{
            
            try {

                    if (p8file.isEmpty() ) {

                        return Response.getFailResponseNonDataBody(ResponseEnum.Message.P8FILENOTFOUND);
                    }
                    if(platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(requestCreateApns.appId,PlatformEnum.Platform.IOS) != null ) {
                        return Response.getFailResponseNonDataBody(ResponseEnum.Message.PLATFORMSETTINGREGISTERED);
                    }
                    String file = FileStorage.uploadFile(p8file);
                    PlatformSettingEntity platformSetting= platformSettingService.saveApns(requestCreateApns.appId, file, requestCreateApns.fileKey, requestCreateApns.teamId, requestCreateApns.bundleId);
                    return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
                
               
            } catch (Exception e) {
                e.printStackTrace();
                return Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
            }
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("/platforms/setting/apns")
    public Object updateApns(@RequestPart(required = true)MultipartFile p8file , RequestCreateApns requestCreateApns   ) throws Exception {
            if (p8file.isEmpty() ) {

                return Response.getFailResponseNonDataBody(ResponseEnum.Message.P8FILENOTFOUND);
            }
            String file = FileStorage.uploadFile(p8file);
            Boolean updateStatus = platformSettingService.updateApns(requestCreateApns.appId, new APNS(file,requestCreateApns.teamId,requestCreateApns.fileKey,requestCreateApns.bundleId));
            return updateStatus ?
                  Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) :
                  Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);

    }

    @DeleteMapping("/platforms/setting/apns")
    public Object deleteApnsConfiguration (@RequestBody RequestRemoveApns requestRemoveApns) {
        try {
            return platformSettingService.removeApnsConfiguration(requestRemoveApns.appId) ? 
                    Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) : 
                    Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);

        } catch (Exception e) {
            return Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);

        }
       
    }
/**
 *
 * FCM
 * */
  
//   @GetMapping("/platforms/setting/fcm")
    public Object getFcm(String appId) throws Exception{

        try {

            PlatformSettingEntity platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.ANDROID);
            PlatformSettingEntity platformSettingWeb = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.WEB);
            
            List<PlatformSettingEntity> platformList = new ArrayList<>();
            if (platformSetting != null  )
                platformList.add(platformSetting);
            if (platformSettingWeb != null  )
                platformList.add(platformSettingWeb);
            return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.SUCCESS, platformList, true));


        } catch (Exception e) {
            return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.FAIL, e.getLocalizedMessage(), false) );
        }
    }

    // @PreAuthorize("@appService.isOwner( authentication.getId(), #appId )")
    @ApiOperation(value = "To register platform setting",notes = "Code = 2 (Fcm android ) & 3 (Fcm Web) ")
    @Transactional(rollbackOn = Exception.class)
    @PostMapping("platforms/setting/fcm")
    public Object saveFcm(@RequestBody RequestCreateFcm requestFcm){
        try {
            if(PlatformEnum.Platform.ANDROID.equals(requestFcm.getPlatformId()) | PlatformEnum.Platform.WEB.equals(requestFcm.getPlatformId()) ) {
                PlatformSettingEntity platformSetting= platformSettingService.saveFcm(requestFcm.getAppId(),requestFcm.getPlatformId(),requestFcm.getAuthorizedKey());
                return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
            }

           return Response.getFailResponseNonDataBody(ResponseEnum.Message.INCORRECTPLATFORM);
        } catch (Exception e) {
          
              return  Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        }
        
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("platforms/setting/fcm")
    public Object updateFcm(@RequestBody RequestUpdateFcm requestFcm){
        try {
            if( PlatformEnum.Platform.ANDROID.equals(requestFcm.getPlatformId()) | PlatformEnum.Platform.WEB.equals(requestFcm.getPlatformId()) ) {

                Boolean updateStatus = platformSettingService.updateFcm(requestFcm.appId, requestFcm.platformId ,requestFcm.authorizedKey);
                return updateStatus ?
                        Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) :
                        Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
            }
            return Response.getFailResponseNonDataBody(ResponseEnum.Message.INCORRECTPLATFORM);
        } catch (Exception e) {

            return Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        }

    }

    @Transactional(rollbackOn = Exception.class)
    @DeleteMapping("/platforms/setting/fcm")
    public Object deleteFcmConfiguration (@RequestBody RequestRemoveFcm requestFcm) {
        try {
            return platformSettingService.removeFcmConfiguration(requestFcm.appId,requestFcm.platformId) ? 
                    Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) : 
                    Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);

        } catch (Exception e) {
            return   Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        }
    }
  
    
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @GetMapping("/platforms")
    public Object get(){
        return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.SUCCESS, platformService.getActivePlatform(), true))  ;
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping("/platforms")
    public Object save (@RequestBody PlatformEntity platform) { 
        PlatformEntity _platform =  platformService.insert(platform);
         return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }

    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PutMapping("/platforms")
    public Object update (@RequestBody PlatformEntity platform) { 

        PlatformEntity _platform =  platformService.update(platform);
        return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @DeleteMapping("/platforms")
    public Object remove (@RequestBody PlatformEntity platform) { 
        Boolean boo = platformService.remove(platform);
        return boo ?  Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) : 
                       Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
    } 
    
    @GetMapping("/push/history")
    public Object getHistory(@RequestParam(required = true) String startDate,@RequestParam(required = true) String endDate,String msgTitle) {

        List<ResponseHistoryDto> listHis = historyService.getAllHistory(startDate, endDate, msgTitle);
        
        return Response.getResponseBody(ResponseEnum.Message.SUCCESS,listHis , true);
    }
    
    @GetMapping("/push/history/{id}")
    public Object displayHistory(@PathVariable("id")Integer id){
     ResponseHistoryDto notiHisto= historyService.getPushNotificationHistoryById(id);
     return Response.getResponseBody(ResponseEnum.Message.SUCCESS,notiHisto, true);
    }
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PostMapping("/create/request")
    public Object  create(String username,String password) throws Exception{
        UserEntity user = userService.saveUserToRequestStatus(username, password);
        return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }
   
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping(value="/{userId}/approval")
    public Object approval(@PathVariable("userId") String userId) throws Exception{
         userService.approveUser(userId);
        return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }
    // @Transactional(rollbackOn = Exception.class)
    // @GetMapping("/devices")
    // public Object getDevice(String appId){
    //         List<Device> devices = deviceService.getActiveDeviceByAppId(appId);
    //         return Response.getResponseBody(ResponseEnum.Message.SUCCESS,devices,true);
    // }

    @ApiOperation("Get Device detail")
    @GetMapping("/devices")
    public Object getDeviceDetail(RequestDevice requestDevice) {

        List<ResponseDevice> listDeviceClients = deviceService.getAllDevicesClient(requestDevice.appId,requestDevice.startDate, requestDevice.endDate, requestDevice.push_id,requestDevice.modelName, requestDevice.plat_code, requestDevice.os_version);
        
        return Response.getResponseBody(ResponseEnum.Message.SUCCESS,listDeviceClients , true);
    }
}