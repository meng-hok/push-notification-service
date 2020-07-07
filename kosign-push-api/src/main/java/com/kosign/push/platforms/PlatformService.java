package com.kosign.push.platforms;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.kosign.push.platformSetting.PlatformSettingRepository;
import com.kosign.push.utils.KeyConf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/***
 * Moving from direct active query
 *   KeyConf.Status.DISABLED.equals()
 */
@Service
public class PlatformService {
    
    @Autowired
    PlatformRepository platformRepository;
    
    private Platform getActivePlatformById(String id) { 
        Platform platform = platformRepository.getOne(id);
       
        return KeyConf.Status.DISABLED.equals(platform.getStatus())? 
              null : platform;
    }
   
    public List<Platform> getActivePlatform() {
        return platformRepository.findAll().stream()
                .filter(platform -> ! KeyConf.Status.DISABLED.equals(platform.getStatus()))
                .collect(Collectors.toList()); 
	}
   
    public Platform insert(Platform platform){

       return platformRepository.save(platform);
    }
    @Transactional
    public Platform update(Platform platform) { 

        Platform prePlatform = this.getActivePlatformById(platform.getId());
        prePlatform.setToNewPlatform(platform);

        return platformRepository.save(prePlatform);
    }
   
    public Boolean remove(Platform platform) { 
        Platform prePlatform = this.getActivePlatformById(platform.getId());
     
        prePlatform.setStatus(KeyConf.Status.DISABLED);
        platformRepository.save(platform);
        return true;
    }

	
}