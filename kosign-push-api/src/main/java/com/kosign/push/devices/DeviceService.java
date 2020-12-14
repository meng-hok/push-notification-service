package com.kosign.push.devices;

import java.util.*;
import java.util.stream.*;

import com.github.pagehelper.*;
import com.kosign.push.apps.dto.*;
import com.kosign.push.devices.dto.Agent;
import com.kosign.push.devices.dto.RequestDeviceList;
import com.kosign.push.devices.dto.ResponseDevice;
import com.kosign.push.utils.enums.ExceptionEnum;
import com.kosign.push.utils.enums.KeyConfEnum;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeviceService {

    private Logger logger = LoggerFactory.getLogger(DeviceService.class);
    @Autowired
    private DeviceRepository deviceRepo;
    @Autowired
    private DeviceBatisRepository deviceBatisRepo;
    
    // public List<Device> getDevicesByUserId(Integer userId){
    // //    return deviceRepo.findByUserIdAndStatus(userId, KeyConf.Status.ACTIVE);
    //     return null;
    // }

    // public Device getActiveDeviceByDeviceIdAndAppId(String deviceId,String appId){
    //     logger.info("[ Request To Repository ]");
    //     Device device = deviceRepo.findByDeviceIdAndAppId(deviceId,appId);
    //     System.out.println(device);
    //     // if(device.getStatus().equals(KeyConf.Status.DISABLED)){
    //     //     return null;
    //     // }
    //     return device;
    // }

    public List<Agent> getActiveDevicesByDeviceIdListAndAppId(ArrayList<String> deviceUniqueIdList , String appId ) {

        return  deviceBatisRepo.findByDeviceIdListAndAppIdRaw(deviceUniqueIdList,appId);

    }

    public List<Agent>  getActiveDeviceByAppIdRaw(String appId){

        List<Agent> agents = deviceBatisRepo.findAllDeviceByAppIdRaw(appId);
        return agents;
    }
    public List<ResponseDevice> getAllDevicesClient(String appId ,String startDate, String endDate, String token, String modelName, String platform, String os  ){
        return deviceBatisRepo.findAllDevicesClient(appId,startDate, endDate, token, modelName, platform, os);
    }

    public Integer countActiveDeviceByAppAndDeviceIdAndToken(String appId,String deviceId,String token){
        // long countByDept(String deptName);
        Integer deviceCount = deviceRepo.countByAppIdAndDeviceIdAndTokenAndStatus(appId, deviceId, token, KeyConfEnum.Status.ACTIVE);
        return deviceCount;
    }

    public List<Agent>   getActiveDeviceByDeviceIdAndAppIdRaw(String deviceId,String appId){

    
        return deviceBatisRepo.findByDeviceIdAndAppIdRaw(deviceId, appId,"");
    }


    public List<Agent>  getActiveDeviceByDeviceIdAndAppIdRaw(String deviceId,String appId,String token){

        return deviceBatisRepo.findByDeviceIdAndAppIdRaw(deviceId, appId,token);
    }
    public List<DeviceEntity> getActiveDeviceByAppId (String appId) {
        List<DeviceEntity> devices= deviceRepo.findByAppIdAndStatus(appId, KeyConfEnum.Status.ACTIVE);
        return devices;
    }

    public List<DeviceEntity> getActiveDeviceByAppIdAndPlatformId(String appId,String platFormId){
        List<DeviceEntity> devices= deviceRepo.findByAppIdAndPlatformIdAndStatus(appId,platFormId, KeyConfEnum.Status.ACTIVE);
        return devices;
    }

    public DeviceEntity saveDevice(DeviceEntity device){
     
        DeviceEntity _device = deviceRepo.save(device);
        logger.info(device.toString());
        return _device;
    }
    
    
    /* Get All data of device clients
     * Neng Channa
     */
    public HashMap<String,Object> findAllDeviceClients(RequestDeviceList request,int pageNum,int pageSize)
    {
        List<ResponseDevice> responseDevices;
        PageHelper.startPage(pageNum,pageSize,true);
        PageInfo<ResponseDevice> pageInfos= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->deviceBatisRepo.findAllDeviceClients(request));
        responseDevices=pageInfos.getList().stream().map(x->{
            x.setModel_name(x.getModel_name());
            x.setCreated_at(x.getCreated_at());
            x.setPush_id(x.getPush_id());
            x.setPlat_code(x.getPlat_code());
            x.setOs_version(x.getOs_version());
            return x;

        }).collect(Collectors.toList());
        PageInfoCustome pageInfoCustome=new PageInfoCustome(pageInfos);
        HashMap<String,Object> response= new HashMap<>();
        response.put("pagination",pageInfoCustome);
        response.put("datas",responseDevices);
//        return deviceBatisRepo.findAllDeviceClients(request);
        return response;
    }




   

  }