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

import com.kosign.push.publics.SuperController;
import com.kosign.push.apps.dto.RequestAppList;
import com.kosign.push.apps.dto.RequestAppUpdate;
import com.kosign.push.apps.dto.RequestCreateApp;
import com.kosign.push.apps.dto.ResponseListAppById;
import com.kosign.push.apps.dto.ResponseListApp;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import java.util.List;
import com.kosign.push.users.UserDetail;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.utils.enums.ResponseEnum;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Applications")
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/v1")
public class AppController extends SuperController 
{
    @GetMapping("/applications")
    public Object findAllApplications
    (
    	@RequestParam(value = "name", defaultValue = "") String name
    ) 
    {
        UserDetail userDetail = GlobalMethod.getUserCredential();
        if(userDetail == null)
        {
            return Response.getResponseBody(ResponseEnum.Message.FAIL, "User Id Not Found", false);
        }
        else
        { 
        	RequestAppList request = new RequestAppList();
        	request.setName(name);
        	
            List<ResponseListApp> applications = appService.findAllApplications(userDetail.getId(),request);
            return Response.getResponseBody(ResponseEnum.Message.SUCCESS, applications, true);
        }
    }
    
    @GetMapping("/applications/{id}")
    public Object findAllApplicationById(@PathVariable("id") String id)
    {
        UserDetail userDetail = GlobalMethod.getUserCredential();
        if(userDetail == null)
        {
            return Response.getResponseBody(ResponseEnum.Message.FAIL, "User Id Not Found", false);
        }
        else
        { 
            List<ResponseListAppById> applications = appService.getActiveAppsByAppId(userDetail.getId(),id);
            return Response.getResponseBody(ResponseEnum.Message.SUCCESS, applications, true);
        }
    }

    @PostMapping("/applications")
    public Object createApplication(@RequestBody RequestCreateApp request)
    {
        try 
        {
            if(appService.getAppByNameAndUserId(request.getName()) != null)
            {
                throw new Exception("This application already existed");
            }

            AppEntity app = new AppEntity();
            app.setName(request.getName());
            
            AppEntity responseApp =  appService.save(app);
            
            if(responseApp==null )
            { 
                throw new Exception("Failed to create application");
            }
            
            return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
        } 
        catch (Exception e) 
        {
            System.out.println(e.getLocalizedMessage());
            return  Response.getFailResponseNonDataBody(e.getLocalizedMessage());
        }
      
    }

    @PutMapping("/applications/{id}")
    public Object updateApplicationById(@RequestBody RequestAppUpdate request, @PathVariable("id") String id) throws Exception
    {
        Boolean update = appService.updateApplication(id, request.getName());
        return update ?
        		Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) :
        		Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
    }

    @DeleteMapping("/applications/{id}")
    public Object deleteApplicationById(@PathVariable("id") String id)
    {
    	Boolean update = appService.disableApplication(id);
        return update ?
        		Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) :
        		Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
    }
}