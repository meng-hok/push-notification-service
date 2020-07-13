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

import com.kosign.push.publics.SuperController;
import com.kosign.push.utils.messages.Response;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;

@Api(tags = "Platforms")
// @PreAuthorize("#oauth2.hasScope('READ')")
@PreAuthorize("hasRole('ROLE_OPERATOR')")
@RestController
@RequestMapping("/api/v1")
public class PlatformController extends SuperController 
{
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
    	@RequestBody PlatformEntity platform
    )
    { 
    	platform.setId(id);
    	PlatformEntity response = platformService.update(platform);
    	
        return response != null ?  Response.setResponseEntity(HttpStatus.OK) : 
		       Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    @DeleteMapping("/platforms/{id}")
    public Object deletePlatformById
    (
    	@PathVariable("id")String id,
    	@RequestBody PlatformEntity platform
    )
    { 
    	platform.setId(id);
        PlatformEntity response = platformService.remove(platform);
        
        return response != null ?  Response.setResponseEntity(HttpStatus.OK) : 
                 Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    } 
   
}