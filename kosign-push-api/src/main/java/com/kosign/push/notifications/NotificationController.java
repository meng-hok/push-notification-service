package com.kosign.push.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.devices.dto.*;
import com.kosign.push.notifications.dto.NotificationSendRequest;
import com.kosign.push.platforms.PlatformEntity;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.RabbitSender;
import com.kosign.push.utils.enums.PlatformEnum;
import com.kosign.push.utils.messages.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.kosign.push.configs.aspectAnnotation.AspectPushHeader;

// @AspectObjectApplicationID

/***
 * 
 * Validated By Interceptor
 * com.kosign.push.configs.InterceptorConfiguration
 */
@AspectPushHeader
@Api(tags = "Notifications")
@RestController
@RequestMapping("/api/public")
public class NotificationController 
{
	@Autowired
    public DeviceService deviceService;
	
	@Autowired 
    public RabbitSender rabbitSender;
   
    @Autowired
    private HttpServletRequest request;
	
    Logger logger = LoggerFactory.getLogger(NotificationController.class);
  
    @ApiOperation(value="Subscribe Device To Application" ,notes = "DeviceId is required & CODE 1 : APNS & 2 : FCM & 3 : FCM WEB ")
    @PostMapping("/devices/subscribe")
    public Object createDevice( @Valid @RequestBody final RequestSubscribeDevice requestSubscribeDevice)
    {
        try 
        {
            String appId = GlobalMethod.convertRequestHeaderToApplicationId(this.request);
//            requestSubscribeDevice.setApp_id(appId);
        
            final Integer platformId = new Integer(requestSubscribeDevice.platformId);
            if (platformId < 0 | platformId > 3 ) 
            { 
                return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
            }
            //validattion
            List<Agent>  agent =  deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(requestSubscribeDevice.getDeviceId(),appId,requestSubscribeDevice.getPushId());
            if (agent.size() > 0 ) 
            { 
                return  Response.setResponseEntity(HttpStatus.CONFLICT);
            }
            final DeviceEntity device = deviceService.saveDevice(new DeviceEntity(requestSubscribeDevice.getDeviceId(),requestSubscribeDevice.getPushId(),new AppEntity(appId),new PlatformEntity( requestSubscribeDevice.getPlatformId() )));
            System.out.println(device);
           
            return ( device != null ) ? 
                Response.setResponseEntity(HttpStatus.OK) : 
                Response.setResponseEntity(HttpStatus.NOT_MODIFIED);
        } 
        catch (final Exception e) 
        {
            e.printStackTrace();
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }

    }
    
    @ApiOperation( value = "Send Notification To Single Device")
    @PostMapping("/devices/notifications/single")
    public Object sendByDevice( @Valid @RequestBody final RequestAgent agentBody)
    {
      
        try 
        {
            String appId = GlobalMethod.convertRequestHeaderToApplicationId(this.request);
//            agentBody.setApp_id(appId);
            
            final List<Agent>  agents = deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(agentBody.getDeviceId(),appId);

            if (agents.isEmpty()) 
            {  
                return Response.setResponseEntity(HttpStatus.NOT_FOUND);
            }

            String bulkId = UUID.randomUUID().toString();

            agents.forEach(agent -> {
                rabbitSender.sendNotifcationByAgent(agent, appId, agentBody.title, agentBody.message,bulkId);
            });

            Map respData = new HashMap<String,Object>();
            respData.put("bulk_id",bulkId);
            respData.put("count",agents.size());
            
            return  Response.setResponseEntity(HttpStatus.OK,respData);
      
        }  catch (final Exception e) 
        {
            logger.info(e.getMessage());
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
      
    }

    @ApiOperation( value = "Send Notification To Device List",notes = "deviceIdList : [ASDQWE,WEQSADZZXC,QWEQE]")
    @PostMapping("/devices/notifications/groups")
    public Object sendByGroup( @Valid @RequestBody final RequestPushDevice requestDevice)
    {
        try 
        {
            String appId = GlobalMethod.convertRequestHeaderToApplicationId(this.request);
//            requestDevice.setApp_id(appId);

            final List<Agent> devices = deviceService.getActiveDevicesByDeviceIdListAndAppId(requestDevice.getDeviceIdList(),appId);
            if (devices.isEmpty())
                return Response.setResponseEntity(HttpStatus.NOT_FOUND);

            String bulkId = UUID.randomUUID().toString();
            for (final Agent device : devices) 
            {
                
                try
                {
                    rabbitSender.sendNotifcationByAgent(new NotificationSendRequest(device, appId, requestDevice.getTitle(), requestDevice.getMessage(),requestDevice.getImage(),requestDevice.getBadgeCount(),bulkId ,requestDevice.getActionType()));

                }
                catch(final Exception e)
                {
                    logger.info("Error Message");
                    System.out.println(e.getMessage());
                
                }
            }

            Map respData = new HashMap<String,Object>();
            respData.put("bulk_id",bulkId);
            respData.put("count",devices.size());
            return  Response.setResponseEntity(HttpStatus.OK,respData);
        } catch (final Exception e) 
        {
            logger.info(e.getMessage());
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation( value = "Send Notification To Device List",notes = "deviceIdList : [ASDQWE,WEQSADZZXC,QWEQE]")
    @PostMapping("/devices/notifications/groups/development")
    public Object sendByGroupDev( @Valid @RequestBody final RequestPushDevice requestDevice)
    {
        try 
        {
            String appId = GlobalMethod.convertRequestHeaderToApplicationId(this.request);
//            requestDevice.setApp_id(appId);

            final List<Agent> devices = deviceService.getActiveDevicesByDeviceIdListAndAppId(requestDevice.getDeviceIdList(),appId);
            if (devices.isEmpty())
                return Response.setResponseEntity(HttpStatus.NOT_FOUND);

            String bulkId = UUID.randomUUID().toString();
            for (final Agent device : devices) 
            {
                
                try
                {
                    if(PlatformEnum.Platform.IOS.equals(device.getPlatform_id()))
                        device.setPlatform_id(PlatformEnum.Platform.IOS_DEV_MODE);
                    rabbitSender.sendNotifcationByAgent(new NotificationSendRequest(device, appId, requestDevice.getTitle(), requestDevice.getMessage(),requestDevice.getImage(),requestDevice.getBadgeCount(),bulkId,requestDevice.getActionType()));
                }
                catch(final Exception e)
                {
                    logger.info("Error Message");
                    System.out.println(e.getMessage());
                
                }
            }

            Map respData = new HashMap<String,Object>();
            respData.put("bulk_id",bulkId);
            respData.put("count",devices.size());
            return  Response.setResponseEntity(HttpStatus.OK,respData);
        } catch (final Exception e) 
        {
            logger.info(e.getMessage());
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation( value = "Send Notification To All Device")
    @PostMapping("/devices/notifications")
    public Object sendByAll(@Valid @RequestBody final RequestPushAgentAll agentBody)
    {
        try 
        {
            String appId = GlobalMethod.convertRequestHeaderToApplicationId(this.request);
//            agentBody.setApp_id(appId);

            final List<Agent> agents = deviceService.getActiveDeviceByAppIdRaw(appId);

            if ( agents.isEmpty())
                return Response.setResponseEntity(HttpStatus.NOT_FOUND);

            String bulkId = UUID.randomUUID().toString();

            for(final Agent agent : agents)
            {
                rabbitSender.sendNotifcationByAgent(new NotificationSendRequest(agent, appId, agentBody.getTitle(), agentBody.getMessage(),agentBody.getImage(),agentBody.getBadgeCount(),bulkId));
            };
            logger.info("Push Done");

            Map respData = new HashMap<String,Object>();
            respData.put("bulk_id",bulkId);
            respData.put("count",agents.size());
            return Response.setResponseEntity(HttpStatus.OK,respData);
     
        } catch (final Exception e) 
        {
            logger.info(e.getMessage()); 
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}