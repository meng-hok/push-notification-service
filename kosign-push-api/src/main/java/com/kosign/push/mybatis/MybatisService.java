package com.kosign.push.mybatis;

import com.kosign.push.utils.messages.ApplicationResponse;
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
}
