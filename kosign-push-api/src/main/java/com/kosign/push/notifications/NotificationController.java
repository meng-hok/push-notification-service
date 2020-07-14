package com.kosign.push.notifications;

import java.util.List;
import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.devices.dto.RequestPushDevice;
import com.kosign.push.devices.dto.Agent;
import com.kosign.push.devices.dto.AgentIdentifier;
import com.kosign.push.devices.dto.RequestAgent;
import com.kosign.push.devices.dto.RequestPushAgentAll;
import com.kosign.push.platforms.PlatformEntity;
import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.messages.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import  com.kosign.push.configs.aspectAnnotation.AspectObjectApplicationID;

@AspectObjectApplicationID
@Api(tags = "Notifications")
@RestController
@RequestMapping("/api/public")
public class NotificationController 
{
	@Autowired
    public DeviceService deviceService;
	
	@Autowired 
    public RabbitSender rabbitSender;
	
    Logger logger = LoggerFactory.getLogger(NotificationController.class);
  
    @ApiOperation(value="Subscribe Device To Application" ,notes = "DeviceId is required & CODE 1 : APNS & 2 : FCM & 3 : FCM WEB ")
    @PostMapping("/devices/subscribe")
    public Object createDevice(@RequestBody final AgentIdentifier agentIdentifier)
    {
        try 
        {
            final Integer platformId = new Integer(agentIdentifier.platform_id);
            if (platformId < 0 | platformId > 3 ) 
            { 
                return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
            }

            Agent agent =  deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(agentIdentifier.getDevice_id(),agentIdentifier.getApp_id());
            if (agent != null ) 
            { 
                return  Response.setResponseEntity(HttpStatus.NOT_FOUND);
            }
            final DeviceEntity device = deviceService.saveDevice(new DeviceEntity(agentIdentifier.getDevice_id(),agentIdentifier.getToken(),new AppEntity(agentIdentifier.getApp_id()),new PlatformEntity( agentIdentifier.getPlatform_id() )));
            System.out.println(device);
           
            return ( device != null ) ? 
                Response.setResponseEntity(HttpStatus.OK) : 
                Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        } 
        catch (final Exception e) 
        {
            e.printStackTrace();
            return Response.setResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    
    @ApiOperation( value = "Send Notification To Single Device")
    @PostMapping("/devices/notifications/send/single")
    public Object sendByDevice(@RequestBody final RequestAgent agentBody) 
    {
        try 
        {
            final Agent agent = deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(agentBody.getDevice_id(),agentBody.getApp_id());
            if(agent == null) 
            {  
                return Response.setResponseEntity(HttpStatus.NOT_FOUND);
            }
            rabbitSender.sendNotifcationByAgent(agent, agentBody.getApp_id(), agentBody.title, agentBody.message);
            return Response.setResponseEntity(HttpStatus.OK);
        } 
        catch (final Exception e) 
        {
            logger.info(e.getMessage());
            return Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
        }
      
    }

    @ApiOperation( value = "Send Notification To Device List",notes = "deviceIdList : [ASDQWE,WEQSADZZXC,QWEQE]")
    @PostMapping("/devices/notifications/send/groups")
    public Object sendByGroup( @RequestBody final RequestPushDevice requestDevice) 
    {
  
        final List<Agent> devices = deviceService.getActiveDevicesByDeviceIdListAndAppId(requestDevice.getDeviceIdList(),requestDevice.getApp_id());

        for (final Agent device : devices) 
        {
            
            try
            {
                rabbitSender.sendNotifcationByAgent(device, requestDevice.getApp_id(), requestDevice.title, requestDevice.message);
            }
            catch(final Exception e)
            {
                logger.info("Error Message");
                System.out.println(e.getMessage());
              
            }
        }
  
        return  Response.setResponseEntity(HttpStatus.OK);
    }

    @ApiOperation( value = "Send Notification To Single Device")
    @PostMapping("/devices/notifications/send/all")
    public Object sendByAll(@RequestBody final RequestPushAgentAll agentBody)
    {
        try 
        {
            final List<Agent> agents = deviceService.getActiveDeviceByAppIdRaw(agentBody.getApp_id());
        
            for(final Agent agent : agents)
            {
                rabbitSender.sendNotifcationByAgent(agent, agentBody.getApp_id(), agentBody.title, agentBody.message);
            };
            logger.info("Push Done");
          
            return Response.setResponseEntity(HttpStatus.OK);
        } 
        catch (final Exception e) 
        {
            logger.info(e.getMessage()); 
            return Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
        }
    }
}