package com.kosign.push.notifications;
import java.util.List;

import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.devices.dto.RequestPushDevice;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.devices.dto.Agent;
import com.kosign.push.devices.dto.AgentIdentifier;
import com.kosign.push.devices.dto.RequestAgent;
import com.kosign.push.devices.dto.RequestPushAgentAll;
import com.kosign.push.platformSetting.dto.FCM;

import com.kosign.push.platforms.PlatformEntity;
import com.kosign.push.publics.SuperController;
import com.kosign.push.utils.FileStorage;

import com.kosign.push.utils.messages.Response;
import com.kosign.push.utils.enums.ResponseEnum;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import  com.kosign.push.configs.aspectAnnotation.AspectObjectApplicationID;

@AspectObjectApplicationID
@Api(tags = "Notifications")
@RestController
@RequestMapping("/api/public")
public class NotificationController extends SuperController{
   

    Logger logger = LoggerFactory.getLogger(NotificationController.class);
    
    
  
    @ApiOperation(value="Subscribe Device To Application" ,notes = "DeviceId is required & CODE 1 : APNS & 2 : FCM & 3 : FCM WEB ")
    @PostMapping("/devices/create")
    public Object save(@RequestBody final AgentIdentifier agentIdentifier){
      
      
        try {
            final Integer platformId = new Integer(agentIdentifier.platform_id);
            if (platformId < 0 | platformId > 3 ) { 
                return Response.getFailResponseNonDataBody(ResponseEnum.Message.INCORRECT_PLATFORM);
            }

            Agent agent =  deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(agentIdentifier.getDevice_id(),agentIdentifier.getApp_id());
            if (agent != null ) { 
                return Response.getFailResponseNonDataBody(ResponseEnum.Message.REGISTERED_DEVICE);
            }
            final DeviceEntity device = deviceService.saveDevice(new DeviceEntity(agentIdentifier.getDevice_id(),agentIdentifier.getToken(),new AppEntity(agentIdentifier.getApp_id()),new PlatformEntity( agentIdentifier.getPlatform_id() )));
            System.out.println(device);
           
            // return Response.getResponseBody(ResponseEnum.Message.SUCCESS,device  , true);
            return ( device != null ) ? 
             Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS) : 
             Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        } catch (final Exception e) {
            
            e.printStackTrace();
            return Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        }

    }
    
    @ApiOperation( value = "Send Notification To Single Device")
    @PostMapping("/notifications/devices/send/single")
    public Object sendByDevice(@RequestBody final RequestAgent agentBody) {
        try {
            //   System.out.println(app_id + deviceId);
           
            final Agent agent = deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(agentBody.getDevice_id(),agentBody.getApp_id());
           
            String response = null;
             FCM fcm;
            switch (agent.platform_id){
                case "1":

                    final APNS apns = new APNS(FileStorage.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, agentBody.getTitle(), agentBody.getMessage());
                    apns.setApp_id(agentBody.getApp_id());
                    rabbitSender.sendToApns(apns);
                    logger.info("[ Response Sucess : APNS ]");

                    response = agent.toString();

                    break;
            
                case "2" :

                    fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                    fcm.setApp_id(agentBody.getApp_id());
                    rabbitSender.sendToFcm(fcm);
                    logger.info("[ Response Sucess : FCM ]");

                    response=agent.toString();
                    break;
                case "3" :
                    fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                    fcm.setApp_id(agentBody.getApp_id());
                    rabbitSender.sendToFcm(fcm);
                    logger.info("[ Response Sucess : FCM ]");

                    response=agent.toString();
                    break;
                
                default : 
                    throw new Exception("Device Id not found");
            }

            return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
        } catch (final Exception e) {
            logger.info(e.getMessage());
           return Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        }
      
    }

    @ApiOperation( value = "Send Notification To Device List",notes = "deviceIdList : [ASDQWE,WEQSADZZXC,QWEQE]")
    @PostMapping("/notifications/devices/send/groups")
    public Object send( @RequestBody final RequestPushDevice requestDevice) {
        Integer success =0 ;
        Integer fail = 0;
        final List<Agent> devices = deviceService.getActiveDevicesByDeviceIdListAndAppId(requestDevice.getDeviceIdList(),requestDevice.getApp_id());

        for (final Agent device : devices) {
            FCM fcm;
            ++success;
            try{
                switch (device.platform_id){
                    case "1":
                        final APNS apns = new APNS(FileStorage.GETP8FILEPATH+device.getPfilename(),device.getTeam_id(),device.getFile_key(), device.getBundle_id(), device.getToken(), requestDevice.getTitle(), requestDevice.getMessage());
                        apns.setApp_id(requestDevice.getApp_id());
                        rabbitSender.sendToApns(apns);
                        break;
                    case "2":
                        fcm = new FCM(device.getAuthorized_key(), device.getToken(),requestDevice.getTitle(), requestDevice.getMessage());
                        fcm.setApp_id(requestDevice.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        break;
                    case "3":
                        fcm = new FCM(device.getAuthorized_key(), device.getToken(),requestDevice.getTitle(), requestDevice.getMessage());
                        fcm.setApp_id(requestDevice.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        break;
                    default : 
                        logger.info("Out of Platform");
                        --success;
                        ++fail ;
                        break;    
                }
               
            }catch(final Exception e){
                logger.info("Error Message");
                System.out.println(e.getMessage());
                ++fail;
            }
        }
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("success",success) ;
        jsonObject.put("fail",fail) ;
        jsonObject.put("target_devices" , devices.size() );

        return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }

   

    @ApiOperation( value = "Send Notification To Single Device")
    @PostMapping("/notifications/devices/send/all")
    public Object sendAll(@RequestBody final RequestPushAgentAll agentBody){
        try {
            //   System.out.println(app_id + deviceId);
           
            final List<Agent> agents = deviceService.getActiveDeviceByAppIdRaw(agentBody.getApp_id());
            Integer fail = 0;
           
            for(final Agent agent : agents){
                FCM fcm;
                switch (agent.platform_id){
                    case "1":
    
                        final APNS apns = new APNS(FileStorage.GETP8FILEPATH+agent.pfilename,agent.team_id, agent.file_key, agent.bundle_id, agent.token, agentBody.getTitle(), agentBody.getMessage());
                        apns.setApp_id(agentBody.getApp_id());
                        rabbitSender.sendToApns(apns);
                        logger.info("[ Response Sucess : APNS ]");
    
    
                        break;
                
                    case "2" :
    
                        fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                        fcm.setApp_id(agentBody.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        logger.info("[ Response Sucess : FCM ]");
    
                        break;
                    case "3" :
                        fcm = new FCM( agent.authorized_key, agent.token,agentBody.getTitle(), agentBody.getMessage());
                        fcm.setApp_id(agentBody.getApp_id());
                        rabbitSender.sendToFcm(fcm);
                        logger.info("[ Response Sucess : FCM ]");
    
                        break;
                    
                    default : 
                        logger.info("Out of Platform");
                        fail ++;
                        break;
                        // throw new Exception("Device Id not found");
                }

            };
            logger.info("Push Done");
            System.out.println("Device found : " + agents.size());
            System.out.println("Fail : " +fail);
            return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
        } catch (final Exception e) {
            logger.info(e.getMessage()); 
            return Response.getFailResponseNonDataBody(ResponseEnum.Message.FAIL);
        }
      
    }
}