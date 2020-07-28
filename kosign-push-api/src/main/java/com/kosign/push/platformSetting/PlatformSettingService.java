package com.kosign.push.platformSetting;

import java.util.List;

import com.kosign.push.apps.AppEntity;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.enums.ExceptionEnum;
import com.kosign.push.utils.enums.KeyConfEnum;
import com.kosign.push.utils.enums.PlatformEnum;
import com.kosign.push.platformSetting.dto.APNS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PlatformSettingService {
    
    @Autowired
    private PlatformSettingRepository settingRepo;

    Logger logger = LoggerFactory.getLogger(PlatformSettingService.class);

    public List<PlatformSettingEntity> getActivePlatformsConfiguredByAppId(String appId) {
        List<PlatformSettingEntity> platforms = settingRepo.findByApplicationIdAndStatus(appId, KeyConfEnum.Status.ACTIVE);
        return platforms;
    }
    
    public PlatformSettingEntity getActivePlatformConfiguredByAppIdAndPlatFormId(String appId,String platformId){
        PlatformSettingEntity platform = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,platformId, KeyConfEnum.Status.ACTIVE);
//        if(platform == null | platform.getStatus().equals(KeyConf.Status.DISABLED)){
//            return null;
//        }
        return platform;
    }

    public String getFcmAuthorizedKeyByAppId(String appId){
        return settingRepo.findAuthorizedKeyByAppIdAndPlatFormRaw(appId,PlatformEnum.Platform.ANDROID, KeyConfEnum.Status.ACTIVE);
    }

    public PlatformSettingEntity saveApns(String appId, String file, String fileKey, String teamId, String bundleId) throws Exception{
        
        PlatformSettingEntity _platformSetting = this.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,PlatformEnum.Platform.IOS);

        if(_platformSetting != null ){ 
            throw new Exception ("Application Platform Setting already saved");
        }
        
        _platformSetting = new PlatformSettingEntity(GlobalMethod.getIos(),new AppEntity(appId),fileKey,teamId,bundleId,file);
        return settingRepo.save(_platformSetting);
    }

    public PlatformSettingEntity saveFcm(String appId, String platform , String authKey) throws Exception{

        PlatformSettingEntity _platformSetting = this.getActivePlatformConfiguredByAppIdAndPlatFormId(appId,platform) ;

        if(_platformSetting != null ){ 
            throw new Exception (ExceptionEnum.Message.REGISTERED_PLATFORM_SETTING);
        }

        if ( PlatformEnum.Platform.ANDROID.equals(platform)  ) { 
            _platformSetting  = new PlatformSettingEntity(GlobalMethod.getAndroid(),new AppEntity(appId),authKey);
        }else if(PlatformEnum.Platform.WEB.equals(platform)) { 
            _platformSetting  = new PlatformSettingEntity(GlobalMethod.getBrowser(),new AppEntity(appId),authKey);
        }else{
            return null;
        }
       
        return settingRepo.save(_platformSetting);
    }

    public Boolean updateFcm(String appId, String platform,String authKey) {
        PlatformSettingEntity platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,platform, KeyConfEnum.Status.ACTIVE);
        if(platformSetting == null ){
            return false;

        }
        platformSetting.setAuthorizedKey(authKey);
        settingRepo.save(platformSetting);
        return true;
    }

    public Boolean updateApns(String appId, APNS apns){
        PlatformSettingEntity platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,PlatformEnum.Platform.IOS, KeyConfEnum.Status.ACTIVE);
        if(platformSetting == null ){
            return false;

        }
        if(StringUtils.isEmpty(apns.p8file)){
            apns.p8file = platformSetting.getPushUrl();
        }
        platformSetting.setApnsConfiguration(apns.fileKey,apns.teamId,apns.bundleId,apns.p8file);
        settingRepo.save(platformSetting);
        return true;
    }

    public Boolean removeApnsConfiguration(String appId) throws Exception{
        PlatformSettingEntity platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,PlatformEnum.Platform.IOS, KeyConfEnum.Status.ACTIVE);
        platformSetting.setStatus(KeyConfEnum.Status.DISABLED);
         settingRepo.save(platformSetting);
         return true;
    }
    public Boolean removeFcmConfiguration(String appId,String platform) throws Exception {
        PlatformSettingEntity platformSetting;
        if(PlatformEnum.Platform.ANDROID.equals(platform)) { 
            platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,PlatformEnum.Platform.ANDROID, KeyConfEnum.Status.ACTIVE);
        }else if(PlatformEnum.Platform.WEB.equals(platform)) { 
            platformSetting = settingRepo.findByApplicationIdAndPlatformIdAndStatus(appId,PlatformEnum.Platform.WEB, KeyConfEnum.Status.ACTIVE);
        }else {
            throw new Exception("Incorrect Platform Exception");
        }

        platformSetting.setStatus(KeyConfEnum.Status.DISABLED);
        settingRepo.save(platformSetting);
        return true;
    }
  

}