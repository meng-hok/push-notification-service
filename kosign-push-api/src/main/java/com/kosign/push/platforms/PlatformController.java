package com.kosign.push.platforms;
import com.kosign.push.platformSetting.PlatformSettingEntity;
import com.kosign.push.platforms.PlatformEntity;
import com.kosign.push.publics.SuperController;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.utils.enums.PlatformEnum;
import com.kosign.push.utils.enums.ResponseEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.annotations.Api;

@Api(tags = "KOSIGN Push Platform Setting API")
// @PreAuthorize("#oauth2.hasScope('READ')")
@PreAuthorize("hasRole('ROLE_OPERATOR')")
@RestController
@RequestMapping("/api/v1")
public class PlatformController extends SuperController {
  
    @GetMapping("/platforms")
    public Object get(){
        return ResponseEntity.ok(Response.getResponseBody(ResponseEnum.Message.SUCCESS, platformService.getActivePlatform(), true))  ;
    }
    
    @PostMapping("/platforms")
    public Object save (@RequestBody PlatformEntity platform) { 
        PlatformEntity _platform =  platformService.insert(platform);
         return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }

    
    @PutMapping("/platforms")
    public Object update (@RequestBody PlatformEntity platform) { 

        PlatformEntity _platform =  platformService.update(platform);
        return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }
    
    @DeleteMapping("/platforms")
    public Object remove (@RequestBody PlatformEntity platform) { 
        Boolean boo = platformService.remove(platform);
        return boo ?  Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) : 
                       Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
    } 
   
}