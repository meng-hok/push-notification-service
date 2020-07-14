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
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import java.util.List;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.messages.Response;
import org.springframework.web.bind.annotation.*;

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
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
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
    public Object findApplicationById(@PathVariable("id") String id)
    {
        UserDetail userDetail = GlobalMethod.getUserCredential();
        if(userDetail == null)
        {
        	return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
        else
        { 
            List<ResponseListAppById> respData = appService.getActiveAppsByAppId(userDetail.getId(),id);
            return Response.setResponseEntity(HttpStatus.OK, respData);
        }
    }

    @PostMapping("/applications")
    public Object createApplication(@RequestBody RequestCreateApp reqData)
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
            	return Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            return Response.setResponseEntity(HttpStatus.OK);
        } 
        catch (Exception e) 
        {
            System.out.println(e.getLocalizedMessage());
            return Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
      
    }

    @AspectStringApplicationID
    @PutMapping("/applications/{id}")
    public Object updateApplicationById( @PathVariable("id") String id,@RequestBody RequestAppUpdate reqData) throws Exception
    {
        try {
            if(appService.getAppByNameAndUserId(reqData.getName()) != null)
            {
            	return Response.setResponseEntity(HttpStatus.CONFLICT);
            }
            Boolean update = appService.updateApplication(id, reqData.getName());
            return update ?
                    Response.setResponseEntity(HttpStatus.OK):
                    Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return
                 Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
       
    }

    @AspectStringApplicationID
    @DeleteMapping("/applications/{id}")
    public Object deleteApplicationById(@PathVariable("id") String id)
    {
        try {
            Boolean update = appService.disableApplication(id);
            return update ?
                    Response.setResponseEntity(HttpStatus.OK):
                    Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            } catch (Exception e) {
                return
                     Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
                   
    }
}