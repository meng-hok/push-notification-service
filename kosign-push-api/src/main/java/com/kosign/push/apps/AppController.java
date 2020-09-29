/**
 * File Name        	: AppController.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/apps/AppController.java
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

import com.kosign.push.apps.dto.RequestAppList;
import com.kosign.push.apps.dto.RequestAppUpdate;
import com.kosign.push.apps.dto.RequestCreateApp;
import com.kosign.push.apps.dto.ResponseListAppById;
import com.kosign.push.configs.aspectAnnotation.*;
import com.kosign.push.apps.dto.ResponseListApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import java.util.List;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.messages.Response;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Validated
@Api(tags = "Applications")
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/v1")
public class AppController 
{
	@Autowired
    public AppService appService;
	
    @GetMapping("/applications")
    public Object findAllApplications
    (
    	@RequestParam(value = "name", defaultValue = "") String name
    ) 
    {
        UserDetail userDetail = GlobalMethod.getUserCredential();
        if(userDetail == null)
        {
            return Response.setResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        else
        { 
        	RequestAppList reqData = new RequestAppList();
        	reqData.setName(name);
        	
            List<ResponseListApp> respData = appService.findAllApplications(userDetail.getId(),reqData);
            return Response.setResponseEntity(HttpStatus.OK, respData);
        }
    }
    
    @GetMapping("/applications/{id}")
    public Object findApplicationById(@PathVariable(value= "id")  String id)
    {
        UserDetail userDetail = GlobalMethod.getUserCredential();
        if(userDetail == null)
        {
        	return Response.setResponseEntity(HttpStatus.UNAUTHORIZED);
        }
        else
        { 
            List<ResponseListAppById> respData = appService.getActiveAppsByAppId(userDetail.getId(),id);
            return respData.isEmpty() ?  Response.setResponseEntity(HttpStatus.NOT_FOUND, respData) : Response.setResponseEntity(HttpStatus.OK, respData);
        }
    }

    @PostMapping("/applications")
    public Object createApplication(@Valid @RequestBody RequestCreateApp reqData)
    {
        try 
        {
            if(appService.getAppByNameAndUserId(reqData.getName()) != null)
            {
            	return Response.setResponseEntity(HttpStatus.CONFLICT);
            }

            AppEntity app = new AppEntity();
            app.setName(reqData.getName());
            
            AppEntity respData =  appService.save(app);
            
            if(respData==null )
            { 
            	return Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
            }
            
            return Response.setResponseEntity(HttpStatus.OK);
        } 
        catch (Exception e) 
        {
            System.out.println(e.getLocalizedMessage());
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
      
    }

    @AspectStringApplicationID
    @PatchMapping("/applications/{id}")
    public Object updateApplicationById(
            @PathVariable("id") String id,
             @Valid @RequestBody RequestAppUpdate reqData) throws Exception
    {
        try {
            if(appService.getAppByNameAndUserId(reqData.getName()) != null)
            {
            	return Response.setResponseEntity(HttpStatus.CONFLICT);
            }
            Boolean update = appService.updateApplication(id, reqData.getName());
            return update ?
                    Response.setResponseEntity(HttpStatus.OK):
                    Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return
                 Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
       
    }

    @AspectStringApplicationID
    @DeleteMapping("/applications/{id}")
    public Object deleteApplicationById(@PathVariable("id") @NotEmpty String id)
    {
        try {
            Boolean update = appService.disableApplication(id);
            return update ?
                    Response.setResponseEntity(HttpStatus.OK):
                    Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
            } catch (Exception e) {
                return
                     Response.setResponseEntity(HttpStatus.BAD_REQUEST);
            }
                   
    }
}