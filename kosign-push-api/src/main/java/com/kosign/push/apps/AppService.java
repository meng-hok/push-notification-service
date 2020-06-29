package com.kosign.push.apps;

import java.security.Key;
import java.util.List;

import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.utils.KeyConf;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
@AllArgsConstructor
@Service
public class AppService {
    

    private AppRepository appRepo;

    private PlatformSettingService platformSettingService;

    public Application save(Application app){
        return appRepo.save(app);
    }

    public List<Application> getAllApps(){
        return appRepo.findByStatus(KeyConf.Status.ACTIVE);
    }
    
    public List<Application> getActiveAppsByUserId(String userId){
        return appRepo.findByUserIdAndStatus(userId,KeyConf.Status.ACTIVE);
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

    public Application getActiveAppDetail(String appId) {

       return appRepo.findByIdAndStatus(appId,KeyConf.Status.ACTIVE);

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