package com.kosign.push.mybatis;

import com.kosign.push.utils.messages.ApplicationResponse;
import com.kosign.push.utils.messages.ApplicationResponseById;
import com.kosign.push.utils.messages.PlatformSettingRespone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MybatisService {

    @Autowired
    MyBatisRepository myBatisRepository;

    public List<ApplicationResponse> getActiveAppsByUserId(String userId){
        List<ApplicationResponse> applicationResponses =  myBatisRepository.findActiveByUserId(userId);

        applicationResponses = applicationResponses.stream().map(application -> {
            application.setPlatform(application.getAndroid() + application.getIos() + application.getFcm());

            return application;
        }).collect(Collectors.toList());
        return applicationResponses;
    }
    public List<ApplicationResponseById> getActiveAppsByAppId(String userId, String appId){
        List<ApplicationResponseById> applicationResponses =  myBatisRepository.findActiveByAppId(userId,appId);
        List<PlatformSettingRespone> platformSettingRespones = myBatisRepository.findPlatformrByAppId(appId);

        applicationResponses = applicationResponses.stream().map(application -> {
            application.setPlatform(application.getAndroid() + application.getIos() + application.getFcm());
            application.setPlatRec(platformSettingRespones);
            return application;
        }).collect(Collectors.toList());
        return applicationResponses;
    }
}
