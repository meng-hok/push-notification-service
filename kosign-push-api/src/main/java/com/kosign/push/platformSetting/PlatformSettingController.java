package com.kosign.push.platformSetting;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;
import javax.validation.Valid;

import com.kosign.push.users.UserDetail;
import com.kosign.push.users.UserEntity;
import com.kosign.push.utils.FileStorageUtil;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.utils.enums.ExceptionEnum;
import com.kosign.push.utils.enums.PlatformEnum;
import com.kosign.push.utils.enums.ResponseEnum;
import com.kosign.push.configs.aspectAnnotation.AspectObjectApplicationID;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.dto.RequestCreateApns;
import com.kosign.push.platformSetting.dto.RequestCreateFcm;
import com.kosign.push.platformSetting.dto.RequestRemoveApns;
import com.kosign.push.platformSetting.dto.RequestRemoveFcm;
import com.kosign.push.platformSetting.dto.RequestUpdateFcm;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@AspectObjectApplicationID
@Api(tags = "Platforms Setting ")
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/v1")
public class PlatformSettingController {

    @Autowired
    public PlatformSettingService platformSettingService;

     //    @GetMapping("/platforms/setting")
     public Object getAllPlatformSetting(@RequestParam(required = true) String appId) throws Exception{

        try {

            List<PlatformSettingEntity> platformSettings = platformSettingService.getActivePlatformsConfiguredByAppId(appId);


            return Response.setResponseEntity(HttpStatus.OK);



        } catch (Exception e) {
            return  Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
   
/**
 *
 * FCM
 * */
    @GetMapping("/platforms/setting/apns")
    public Object getApns(@RequestParam(required = true ,name = "app_id") String appId) throws Exception{

        try {

            PlatformSettingEntity platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.IOS);
            return Response.setResponseEntity(HttpStatus.OK,platformSetting);

        } catch (Exception e) {
            return  Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional(rollbackOn = Exception.class)
    @PostMapping( value = "/platforms/setting/apns")
    public Object saveApns( @Valid RequestCreateApns requestCreateApns , @RequestPart(value = "p8_file", required = false)MultipartFile p8file ) throws Exception{
            
            try {

                    if (p8file.isEmpty() ) {

                        return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    if(platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(requestCreateApns.getApp_id(),PlatformEnum.Platform.IOS) != null ) {
                        return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);
                    }
                    String file = FileStorageUtil.uploadFile(p8file);
                    PlatformSettingEntity platformSetting= platformSettingService.saveApns(requestCreateApns.getApp_id(), file, requestCreateApns.file_key, requestCreateApns.team_id, requestCreateApns.bundle_id);
                    return Response.setResponseEntity(HttpStatus.OK);
                
               
            } catch (Exception e) {
                e.printStackTrace();
                return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);
            }
    }

    @Transactional(rollbackOn = Exception.class)
    @PutMapping("/platforms/setting/apns")
    public Object updateApns( @Valid RequestCreateApns requestCreateApns  ,@RequestPart(value = "p8_file",required = false)MultipartFile p8file ) throws Exception {
        String file = "";

        if (p8file != null) {

            file= FileStorageUtil.uploadFile(p8file);
        }

        Boolean updateStatus = platformSettingService.updateApns(requestCreateApns.getApp_id(), new APNS(file,requestCreateApns.team_id,requestCreateApns.file_key,requestCreateApns.bundle_id));
        return updateStatus ?
                Response.setResponseEntity(HttpStatus.OK) :
                Response.setResponseEntity(HttpStatus.NOT_MODIFIED);

    }

    @DeleteMapping("/platforms/setting/apns")
    public Object deleteApnsConfiguration (@Valid @RequestBody RequestRemoveApns requestRemoveApns) {
        try {
            return platformSettingService.removeApnsConfiguration(requestRemoveApns.getApp_id()) ? 
                    Response.setResponseEntity(HttpStatus.OK) : 
                     Response.setResponseEntity(HttpStatus.NOT_MODIFIED);

        } catch (Exception e) {
            return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);

        }
       
    }
/**
 *
 * FCM
 * */
  
   @GetMapping("/platforms/setting/fcm")
    public Object getFcm(@RequestParam(required = true ,name = "app_id") String appId) throws Exception{

        try {

            PlatformSettingEntity platformSetting = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.ANDROID);
            PlatformSettingEntity platformSettingWeb = platformSettingService.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.WEB);
            
            List<PlatformSettingEntity> platformList = new ArrayList<>();
            if (platformSetting != null  )
                platformList.add(platformSetting);
            if (platformSettingWeb != null  )
                platformList.add(platformSettingWeb);
            
            return  Response.setResponseEntity(HttpStatus.OK,platformList);

        } catch (Exception e) {
            return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "To register platform setting",notes = "Code = 2 (Fcm android ) & 3 (Fcm Web) ")
//    @Transactional(rollbackOn = Exception.class)
    @PostMapping("platforms/setting/fcm")
    public Object saveFcm(@Valid @RequestBody RequestCreateFcm requestFcm){
        try {
            if(PlatformEnum.Platform.ANDROID.equals(requestFcm.getPlatformId()) | PlatformEnum.Platform.WEB.equals(requestFcm.getPlatformId()) ) {
                PlatformSettingEntity platformSetting= platformSettingService.saveFcm(requestFcm.getApp_id(),requestFcm.getPlatformId(),requestFcm.getAuthorizedKey());
                return  Response.setResponseEntity(HttpStatus.OK);
            }

           
            return  Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
        
    }

//    @Transactional(rollbackOn = Exception.class)
    @PutMapping("platforms/setting/fcm")
    public Object updateFcm(@Valid @RequestBody RequestUpdateFcm requestFcm){
        try {
            if( PlatformEnum.Platform.ANDROID.equals(requestFcm.getPlatformId()) | PlatformEnum.Platform.WEB.equals(requestFcm.getPlatformId()) ) {

                Boolean updateStatus = platformSettingService.updateFcm(requestFcm.getApp_id(), requestFcm.platformId ,requestFcm.authorizedKey);
                return updateStatus ?
                        Response.setResponseEntity(HttpStatus.OK) :
                         Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
            }
            return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {

            return  Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }

    @Transactional(rollbackOn = Exception.class)
    @DeleteMapping("/platforms/setting/fcm")
    public Object deleteFcmConfiguration ( @Valid @RequestBody RequestRemoveFcm requestFcm) {
        try {
            return platformSettingService.removeFcmConfiguration(requestFcm.getApp_id(),requestFcm.platformId) ? 
                    Response.setResponseEntity(HttpStatus.OK) :
                     Response.setResponseEntity(HttpStatus.NOT_MODIFIED);

        } catch (Exception e) {
            return    Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
  
}