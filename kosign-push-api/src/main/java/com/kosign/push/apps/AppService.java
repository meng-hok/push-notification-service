package com.kosign.push.apps;

import java.security.Key;
import java.util.List;

import com.kosign.push.utils.KeyConf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppService {
    
    @Autowired
    private AppRepository appRepo;

    public Application save(Application app){
        return appRepo.save(app);
    }

    public List<Application> getAllApps(){
        return appRepo.findByStatus(KeyConf.Status.ACTIVE);
    }
    
    public List<Application> getActiveAppsByUserId(String userId){
        return appRepo.findByUserIdAndStatus(userId,KeyConf.Status.ACTIVE);
    }
}