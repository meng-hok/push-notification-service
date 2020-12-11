/**
 * File Name        	: AppService.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/apps/AppService.java
 * File Description 	: 
 * 
 * File Author	  		: Neng Channa
 * Created Date	  	    : 10-July-2020 18:52
 * Developed By	  	    : Sok Menghok
 * Modified Date	  	: 10-July-2020 18:52
 * Modified By          : Sok Menghok
 *
 **/

package com.kosign.push.apps;

import java.util.*;
import java.util.stream.Collectors;

//import com.github.pagehelper.*;
import com.github.pagehelper.*;
import com.github.pagehelper.Page;
import com.kosign.push.apps.dto.*;
import com.kosign.push.platformSetting.PlatformSettingService;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.enums.KeyConfEnum;
import com.kosign.push.platformSetting.dto.ResponsePlatformSetting;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AppService 
{
    private AppRepository appRepo;
    private AppBatisRepository appBatisRepo;
    private PlatformSettingService platformSettingService;

    public HashMap<String,Object> findAllApplications(String userId, RequestAppList request,int pageNum,int pageSize)
    {
//        PageHelper.startPage(pageNum,pageSize);
        PageHelper.startPage(pageNum,pageSize,true);
        List<ResponseListApp> applicationResponses =  appBatisRepo.findAllApplications(userId, request);

//        applicationResponses = applicationResponses.stream().map(application -> {
//            application.setTotalPush(application.getAndroid()  + application.getIos() + application.getFcm());
//            application.setApns( application.getIos() );
//            application.setFcm(application.getAndroid()  + application.getFcm());
//            return application;
//        }).collect(Collectors.toList());

        PageInfo<ResponseListApp> pageInfos= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->appBatisRepo.findAllApplications(userId, request));
        applicationResponses = pageInfos.getList().stream().map(x->{
            x.setTotalPush(x.getAndroid()  + x.getIos() + x.getFcm());
           x.setApns( x.getIos() );
           x.setFcm(x.getAndroid()  + x.getFcm());
           return x;
        }).collect(Collectors.toList());
        PageInfoCustome pageInfoCustome=new PageInfoCustome(pageInfos);
//        List<Object> objects=new ArrayList<>();
        HashMap<String,Object> response= new HashMap<>();
        response.put("pagination",pageInfoCustome);
        response.put("datas",applicationResponses);

//        objects.add(pageInfoCustome);
//        objects.add(applicationResponses);
        return response;
    }
    
    public AppEntity save(AppEntity app)
    {
        return appRepo.save(app);
    }
   
    public AppEntity getActiveAppDetail(String appId) 
    {
        return appRepo.findByIdAndStatus(appId, KeyConfEnum.Status.ACTIVE);
    }
 
    public List<AppEntity> getAllApps()
    {
        return appRepo.findByStatus(KeyConfEnum.Status.ACTIVE);
    }

    public List<AppEntity> getActiveAppsByUserId(String userId)
    {
        return appRepo.findByUserIdAndStatus(userId, KeyConfEnum.Status.ACTIVE);
    }
    
    public List<ResponseListAppById> getActiveAppsByAppId(String userId, String appId)
    {
        List<ResponseListAppById> applicationResponses =  appBatisRepo.findActiveByAppId(userId,appId);
        List<ResponsePlatformSetting> responsePlatformSettings = appBatisRepo.findPlatformrByAppId(appId);

        applicationResponses = applicationResponses.stream().map(application -> {
            application.setPlatform(application.getAndroid() + application.getIos() + application.getFcm());
            application.setPlatRec(responsePlatformSettings);
            return application;
        }).collect(Collectors.toList());
        
        return applicationResponses;
    }

    public String getOwnerIdByAppId(String appId)
    {
        String ownerId = appRepo.findUserIdByAppId(appId, KeyConfEnum.Status.ACTIVE);
        System.out.println(ownerId);
        
        return ownerId;
    }

    public String getAuthorizedKeyByAppId(String appId)
    {
        String authorizedKey = platformSettingService.getFcmAuthorizedKeyByAppId(appId);
        
        return authorizedKey;
    }

   
    public Boolean disableApplication(String appId ) 
    {
        AppEntity application = getActiveAppDetail(appId);
        if(application == null )
        {
            return false;
        }
        application.setStatus(KeyConfEnum.Status.DISABLED);
        appRepo.save(application);
        
        return true;
    }

    public Boolean updateApplication(String appId, String name) throws Exception 
    {
        AppEntity application = getActiveAppDetail(appId);
        if(application == null )
        {
            return false;
        }
        application.setName(name);
        appRepo.save(application);
        
        return true;
    }

    public AppEntity getAppByNameAndUserId(String name) 
    {
        String userId = GlobalMethod.getUserCredential().getId();
        return appRepo.findByUserIdAndNameAndStatus(userId,name, KeyConfEnum.Status.ACTIVE);
    }
}