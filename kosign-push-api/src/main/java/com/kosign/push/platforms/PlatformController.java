/**
 * File Name        	: PlatformController.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/platforms/PlatformController.java
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

import com.kosign.push.platforms.dto.RequestPlatformUpdate;
import com.kosign.push.utils.messages.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

@Api(tags = "Platforms")
// @PreAuthorize("#oauth2.hasScope('READ')")
@PreAuthorize("hasRole('ROLE_OPERATOR')")
@RestController
@RequestMapping("/api/v1")
public class PlatformController
{

    @Autowired
    PlatformService platformService;
    @GetMapping("/platforms")
    public Object findAllPlatforms()
    {
    	return Response.setResponseEntity(HttpStatus.OK, platformService.getActivePlatform());
    }
    
    @PostMapping("/platforms")
    public Object createPlatform(@RequestBody PlatformEntity request) 
    { 
        PlatformEntity response = platformService.insert(request);
        return response != null ?  Response.setResponseEntity(HttpStatus.OK) : 
		       Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @PutMapping("/platforms/{id}")
    public Object updatePlatformById
    (
    	@PathVariable("id")String id, 
    	@RequestBody RequestPlatformUpdate request
    )
    { 
    	PlatformEntity platform = new PlatformEntity();
    	platform.setId(id);
    	platform.setName(request.getName());
    	platform.setCode(request.getCode());
    	platform.setName(request.getIcon());
    	
    	PlatformEntity response = platformService.update(platform);
    	
        return response != null ?  Response.setResponseEntity(HttpStatus.OK) : 
		       Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @DeleteMapping("/platforms/{id}")
    public Object deletePlatformById(@PathVariable("id")String id)
    { 
        PlatformEntity response = platformService.remove(id);
        
        return response != null ?  Response.setResponseEntity(HttpStatus.OK) : 
                 Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    } 
   
}