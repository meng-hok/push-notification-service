/**
 * File Name        	: PlatformService.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/platforms/PlatformService.java
 * File Description 	: 
 * 
 * File Author	  		: Neng Channa
 * Created Date	  	    : 13-July-2020 13:40
 * Developed By	  	    : Sok Menghok
 * Modified Date	  	: 13-July-2020 16:29
 * Modified By          : Sok Menghok
 *
 **/

package com.kosign.push.platforms;

import java.util.List;
import java.util.stream.Collectors;
import com.kosign.push.utils.enums.KeyConfEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlatformService 
{
    @Autowired
    PlatformRepository platformRepository;
    
    private PlatformEntity getActivePlatformById(String id) 
    { 
        PlatformEntity platform = platformRepository.getOne(id);
        return KeyConfEnum.Status.DISABLED.equals(platform.getStatus())? null : platform;
    }
   
    public List<PlatformEntity> getActivePlatform() 
    {
        return platformRepository.findAll().stream()
                .filter(platform -> ! KeyConfEnum.Status.DISABLED.equals(platform.getStatus()))
                .collect(Collectors.toList()); 
	}
   
    public PlatformEntity insert(PlatformEntity platform)
    {
    	return platformRepository.save(platform);
    }

    public PlatformEntity update(PlatformEntity platform) 
    { 
        PlatformEntity prePlatform = this.getActivePlatformById(platform.getId());
        prePlatform.setToNewPlatform(platform);

        return platformRepository.save(prePlatform);
    }
   
    public PlatformEntity remove(PlatformEntity platform) 
    { 
        PlatformEntity prePlatform = this.getActivePlatformById(platform.getId());
        prePlatform.setStatus(KeyConfEnum.Status.DISABLED);
        return platformRepository.save(prePlatform); 
            
    }
}