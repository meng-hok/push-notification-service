/**
 * File Name        	: DeviceController.java
 * File Path        	: /kosign-push-api/src/main/java/com/kosign/push/devices/DeviceController.java
 * File Description 	: 
 * 
 * File Author	  		: Neng Channa
 * Created Date	  	    : 13-July-2020 08:45
 * Developed By	  	    : Sok Menghok
 * Modified Date	  	: 13-July-2020 08:45
 * Modified By          : Sok Menghok
 *
 **/

package com.kosign.push.devices;


import java.util.List;

import com.kosign.push.devices.dto.RequestDeviceList;
import com.kosign.push.devices.dto.ResponseDevice;
import com.kosign.push.utils.messages.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Devices")
@PreAuthorize("#oauth2.hasScope('READ')")
@RestController
@RequestMapping("/api/public")
public class DeviceController
{
	@Autowired
    public DeviceService deviceService;
	
    @ApiOperation("Find All Devices")
    @GetMapping("/devices")
    public Object findAllDevices
    (
    	@RequestParam(value = "app_id"    ) String appId    ,
    	@RequestParam(value = "start_date") String startDate,
    	@RequestParam(value = "end_date"  ) String endDate  ,
    	@RequestParam(value = "model_name", defaultValue = "") String modelName,
    	@RequestParam(value = "os_version", defaultValue = "") String osVersion,
    	@RequestParam(value = "plat_code" , defaultValue = "") String platCode ,
    	@RequestParam(value = "push_id"   , defaultValue = "") String pushId
    ) 
    {
    	RequestDeviceList request = new RequestDeviceList();
    	request.setAppId(appId);
    	request.setStartDate(startDate);
    	request.setEndDate(endDate);
    	request.setModelName(modelName);
    	request.setOsVersion(osVersion);
    	request.setPlatCode(platCode);
    	request.setPushId(pushId);
    	
    	List<ResponseDevice> response = deviceService.findAllDeviceClients(request);
    	return Response.setResponseEntity(HttpStatus.OK, response);
    }
}