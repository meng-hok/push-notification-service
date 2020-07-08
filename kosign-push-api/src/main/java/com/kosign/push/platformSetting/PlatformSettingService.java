package com.kosign.push.platformSetting;

import java.util.List;

import com.kosign.push.apps.AppEntity;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;

import com.kosign.push.platformSetting.dto.APNS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class PlatformSettingService {
    
    @Autowired
    private PlatformSettingRepository settingRepo;

    Logger logger = LoggerFactory.getLogger(PlatformSettingService.class);

    public List<PlatformSetting> getActivePlatformsConfiguredByAppId(String appId) {
        List<PlatformSetting> platforms = settingRepo.findByApplicationIdAndStatus(appId,KeyConf.Status.ACTIVE);
        return platforms;
    }
    
    public PlatformSetting getActivePlatformConfiguredByAppIdAndPlatFormId(String appId,String platformId){
        PlatformSetting platform = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,platformId,KeyConf.Status.ACTIVE);
//        if(platform == null | platform.getStatus().equals(KeyConf.Status.DISABLED)){
//            return null;
//        }
        return platform;
    }

    public String getFcmAuthorizedKeyByAppId(String appId){
        return settingRepo.findAuthorizedKeyByAppIdAndPlatFormRaw(appId,KeyConf.PlatForm.ANDROID,KeyConf.Status.ACTIVE);
    }

    public PlatformSetting saveApns(String appId, String file, String fileKey, String teamId, String bundleId) throws Exception{
        
        PlatformSetting _platformSetting = this.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,KeyConf.PlatForm.IOS);

        if(_platformSetting != null ){ 
            throw new Exception ("Application Platform Setting already saved");
        }
        
        _platformSetting = new PlatformSetting(GlobalMethod.getIos(),new AppEntity(appId),fileKey,teamId,bundleId,file);
        return settingRepo.save(_platformSetting);
    }

    public PlatformSetting saveFcm(String appId, String platform , String authKey) throws Exception{

        PlatformSetting _platformSetting = this.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,platform) ;

        if(_platformSetting != null ){ 
            throw new Exception ("Application Platform Setting already saved");
        }

        if ( KeyConf.PlatForm.ANDROID.equals(platform)  ) { 
            _platformSetting  = new PlatformSetting(GlobalMethod.getAndroid(),new AppEntity(appId),authKey);
        }else if(KeyConf.PlatForm.WEB.equals(platform)) { 
            _platformSetting  = new PlatformSetting(GlobalMethod.getBrowser(),new AppEntity(appId),authKey);
        }else{
            return null;
        }
       
        return settingRepo.save(_platformSetting);
    }

    public Boolean updateFcm(String appId, String authKey) {
        PlatformSetting platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,KeyConf.PlatForm.ANDROID,KeyConf.Status.ACTIVE);
        if(platformSetting == null ){
            return false;

        }
        platformSetting.setAuthorizedKey(authKey);
        settingRepo.save(platformSetting);
        return true;
    }

    public Boolean updateApns(String appId, APNS apns){
        PlatformSetting platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,KeyConf.PlatForm.IOS,KeyConf.Status.ACTIVE);
        if(platformSetting == null ){
            return false;

        }
        platformSetting.setApnsConfiguration(apns.fileKey,apns.teamId,apns.bundleId,apns.p8file);
        settingRepo.save(platformSetting);
        return true;
    }

    public Boolean removeApnsConfiguration(String appId) throws Exception{
        PlatformSetting platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,KeyConf.PlatForm.IOS,KeyConf.Status.ACTIVE);
        platformSetting.setStatus(KeyConf.Status.DISABLED);
         settingRepo.save(platformSetting);
         return true;
    }
    public Boolean removeFcmConfiguration(String appId,String platform) throws Exception {
        PlatformSetting platformSetting;
        if(KeyConf.PlatForm.ANDROID.equals(platform)) { 
            platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,KeyConf.PlatForm.ANDROID,KeyConf.Status.ACTIVE);
        }else if(KeyConf.PlatForm.WEB.equals(platform)) { 
            platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,KeyConf.PlatForm.WEB,KeyConf.Status.ACTIVE);
        }else {
            throw new Exception("Incorrect Platform Exception");
        }

        platformSetting.setStatus(KeyConf.Status.DISABLED);
        settingRepo.save(platformSetting);
        return true;
    }
  

}