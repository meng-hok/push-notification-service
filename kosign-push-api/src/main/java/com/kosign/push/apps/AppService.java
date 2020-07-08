package com.kosign.push.apps;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.utils.KeyConf;

import com.kosign.push.apps.dto.ResponseAppById;
import com.kosign.push.apps.dto.ResponseListApp;
import com.kosign.push.utils.messages.PlatformSettingRespone;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class AppService {

    private AppRepository appRepo;
    private AppMybatisRepository appMybatisRepo;
    private PlatformSettingService platformSettingService;

    public AppEntity save(AppEntity app){
        return appRepo.save(app);
    }
   
    public AppEntity getActiveAppDetail(String appId) {

        return appRepo.findByIdAndStatus(appId,KeyConf.Status.ACTIVE);
 
     }
 
    public List<AppEntity> getAllApps(){
        return appRepo.findByStatus(KeyConf.Status.ACTIVE);
    }

    public List<AppEntity> getActiveAppsByUserId(String userId){
        return appRepo.findByUserIdAndStatus(userId,KeyConf.Status.ACTIVE);
    }
    
    public List<ResponseListApp> getActiveAppsByUserIdAndName(String userId,String appName){
        List<ResponseListApp> applicationResponses =  appMybatisRepo.findActiveByUserIdAndName(userId,appName);

        applicationResponses = applicationResponses.stream().map(application -> {
            application.setTotalPush(application.getAndroid()  + application.getIos() + application.getFcm());
            application.setApns( application.getIos() ); 
            application.setFcm(application.getAndroid()  + application.getFcm());
            return application;
        }).collect(Collectors.toList());
        return applicationResponses;
    }

    public List<ResponseAppById> getActiveAppsByAppId(String userId, String appId){
        List<ResponseAppById> applicationResponses =  appMybatisRepo.findActiveByAppId(userId,appId);
        List<PlatformSettingRespone> platformSettingRespones = appMybatisRepo.findPlatformrByAppId(appId);

        applicationResponses = applicationResponses.stream().map(application -> {
            application.setPlatform(application.getAndroid() + application.getIos() + application.getFcm());
            application.setPlatRec(platformSettingRespones);
            return application;
        }).collect(Collectors.toList());
        return applicationResponses;
    }

    public String getOwnerIdByAppId(String appId){
        
        String ownerId = appRepo.findUserIdByAppId(appId,KeyConf.Status.ACTIVE);
        System.out.println(ownerId);
        return ownerId;
    }

    public String getAuthorizedKeyByAppId(String appId){

        String authorizedKey = platformSettingService.getFcmAuthorizedKeyByAppId(appId);
        return authorizedKey;
    }

   
    public Boolean disableApplication(String appId ) {
        AppEntity application = getActiveAppDetail(appId);
        if(application == null ){
            return false;
        }
        application.setStatus(KeyConf.Status.DISABLED);
        appRepo.save(application);
        return true;
    }

    public Boolean updateApplication(String appId, String name) throws Exception {
        AppEntity application = getActiveAppDetail(appId);
        if(application == null ){
            return false;
        }
        application.setName(name);
        appRepo.save(application);
        return true;
    }
}