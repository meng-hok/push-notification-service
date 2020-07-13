package com.kosign.push.devices;


import java.util.List;

import com.kosign.push.devices.dto.RequestDevice;
import com.kosign.push.devices.dto.ResponseDevice;
import com.kosign.push.publics.SuperController;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.utils.enums.ResponseEnum;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = " Devices ")
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/v1")
public class DeviceController extends SuperController {
     // @Transactional(rollbackOn = Exception.class)
    // @GetMapping("/devices")
    // public Object getDevice(String appId){
    //         List<Device> devices = deviceService.getActiveDeviceByAppId(appId);
    //         return Response.getResponseBody(ResponseEnum.Message.SUCCESS,devices,true);
    // }
    @ApiOperation("Get Device detail")
    @GetMapping("/devices/client")
    public Object getDeviceDetail(  @RequestParam(required = true) String appId,@RequestParam(required = true)String startDate, @RequestParam(required = true) String endDate, RequestDevice requestDevice) {

        List<ResponseDevice> listDeviceClients = deviceService.getAllDevicesClient(appId,startDate, endDate, requestDevice.push_id,requestDevice.modelName, requestDevice.plat_code, requestDevice.os_version);
        
        return Response.getResponseBody(ResponseEnum.Message.SUCCESS,listDeviceClients , true);
    }
}