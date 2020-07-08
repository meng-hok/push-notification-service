package com.kosign.push.apps;

import java.security.Key;
import java.util.List;
import java.util.stream.Collectors;

import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.utils.KeyConf;

import com.kosign.push.apps.dto.ApplicationIdentifier;
import com.kosign.push.apps.dto.ApplicationResponse;
import com.kosign.push.apps.dto.ApplicationResponseById;
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

    public Application save(Application app){
        return appRepo.save(app);
    }
   
    public Application getActiveAppDetail(String appId) {

        return appRepo.findByIdAndStatus(appId,KeyConf.Status.ACTIVE);
 
     }
 
    public List<Application> getAllApps(){
        return appRepo.findByStatus(KeyConf.Status.ACTIVE);
    }

    public List<Application> getActiveAppsByUserId(String userId){
        return appRepo.findByUserIdAndStatus(userId,KeyConf.Status.ACTIVE);
    }
    
    public List<ApplicationResponse> getActiveAppsByUserIdAndName(String userId,String appName){
        List<ApplicationResponse> applicationResponses =  appMybatisRepo.findActiveByUserIdAndName(userId,appName);

        applicationResponses = applicationResponses.stream().map(application -> {
            application.setTotalPush(application.getAndroid()  + application.getIos() + application.getFcm());
            application.setApns( application.getIos() ); 
            application.setFcm(application.getAndroid()  + application.getFcm());
            return application;
        }).collect(Collectors.toList());
        return applicationResponses;
    }

    public List<ApplicationResponseById> getActiveAppsByAppId(String userId, String appId){
        List<ApplicationResponseById> applicationResponses =  appMybatisRepo.findActiveByAppId(userId,appId);
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
        Application application = getActiveAppDetail(appId);
        if(application == null ){
            return false;
        }
        application.setStatus(KeyConf.Status.DISABLED);
        appRepo.save(application);
        return true;
    }

    public Boolean updateApplication(String appId, String name) throws Exception {
        Application application = getActiveAppDetail(appId);
        if(application == null ){
            return false;
        }
        application.setName(name);
        appRepo.save(application);
        return true;
    }
}