package com.kosign.push.devices;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosign.push.messages.Agent;
import com.kosign.push.utils.KeyConf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private Logger logger = LoggerFactory.getLogger(DeviceService.class);
    @Autowired
    private DeviceRepository deviceRepo;
    
    public List<Device> getDevicesByUserId(Integer userId){
    //    return deviceRepo.findByUserIdAndStatus(userId, KeyConf.Status.ACTIVE);
        return null;
    }

    public Device getActiveDeviceByDeviceIdAndAppId(String deviceId,String appId){
        logger.info("[ Request To Repository ]");
        Device device = deviceRepo.findByDeviceIdAndAppId(deviceId,appId);
        System.out.println(device);
        // if(device.getStatus().equals(KeyConf.Status.DISABLED)){
        //     return null;
        // }
        return device;
    }


    public List<Device> getActiveDeviceByAppId (String appId) {
        List<Device> devices= deviceRepo.findByAppIdAndStatus(appId,KeyConf.Status.ACTIVE);
        return devices;
    }

    public List<Device> getActiveDeviceByAppIdAndPlatformId(String appId,String platFormId){
        List<Device> devices= deviceRepo.findByAppIdAndPlatformIdAndStatus(appId,platFormId,KeyConf.Status.ACTIVE);
        return devices;
    }

    public Device saveDevice(Device device){
        Device _device = deviceRepo.save(device);

        return _device;
    }

    public Agent  getActiveDeviceByDeviceIdAndAppIdRaw(String deviceId,String appId){

        List<Map<String,String>> maps = deviceRepo.findByDeviceIdAndAppIdRaw(deviceId, appId);
        if(maps.size() > 0 ){
             // Agent  agent =  gson.from (jsonElement, MyPojo.class);
            ObjectMapper mapper = new ObjectMapper();
            Agent agent = mapper.convertValue(maps.get(0), Agent.class);
            return agent;
        }
        return null;
    }

    public List<Map<String,String>>  getActiveDeviceByAppIdRaw(String appId){

        List<Map<String,String>> maps = deviceRepo.findByAppIdRaw(appId);
        return maps;
    }
}