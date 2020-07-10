package com.kosign.push.platforms;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.kosign.push.utils.enums.KeyConfEnum;

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
    
    private PlatformEntity getActivePlatformById(String id) { 
        PlatformEntity platform = platformRepository.getOne(id);
       
        return KeyConfEnum.Status.DISABLED.equals(platform.getStatus())?
              null : platform;
    }
   
    public List<PlatformEntity> getActivePlatform() {
        return platformRepository.findAll().stream()
                .filter(platform -> ! KeyConfEnum.Status.DISABLED.equals(platform.getStatus()))
                .collect(Collectors.toList()); 
	}
   
    public PlatformEntity insert(PlatformEntity platform){

       return platformRepository.save(platform);
    }
    @Transactional
    public PlatformEntity update(PlatformEntity platform) { 

        PlatformEntity prePlatform = this.getActivePlatformById(platform.getId());
        prePlatform.setToNewPlatform(platform);

        return platformRepository.save(prePlatform);
    }
   
    public Boolean remove(PlatformEntity platform) { 
        PlatformEntity prePlatform = this.getActivePlatformById(platform.getId());
     
        prePlatform.setStatus(KeyConfEnum.Status.DISABLED);
        platformRepository.save(platform);
        return true;
    }

	
}