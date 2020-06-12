package com.kosign.push;

import java.util.List;

import com.kosign.push.apps.Application;
import com.kosign.push.devices.Device;
import com.kosign.push.devices.DeviceService;
import com.kosign.push.apps.AppService;
import com.kosign.push.utils.HttpClient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Controller
public class HomeController {
    
    Logger logger = LoggerFactory.getLogger(HomeController.class);
    @Autowired
    private DeviceService deviceService;
   
    @Autowired
    private AppService appService;

    // @GetMapping({"/home",""})
    // public String home(Model model){
    //     model.addAttribute("PROJECTS", projectService.getAllProjects());
    //     return "components/home";
    // }
  

    @GetMapping("/form/projects")
    public String project(){
        return "components/project";
    }

    @GetMapping("/form/users")
    public String users(){
        return "components/user";
    }
    @GetMapping("/devices")
    public String deivces(Model model){
        List<Device> devices=  deviceService.getActiveDeviceByAppId("1");
        model.addAttribute("DEVICES", devices);
        return "components/device";
    }

    // @GetMapping("/form/apps")
    // public String app(Model model){
    //     model.addAttribute("PROJECTS", projectService.getAllProjects());
    //     return "components/app";
    // }

    @ResponseBody
    @PreAuthorize("#oauth2.hasScope('read')")
    @GetMapping("/index")
    public Object index(){

        HttpClient http = new HttpClient();
        return http.get("https://jsonplaceholder.typicode.com/todos/1").toMap();
        
      
    }
    @ResponseBody
    @GetMapping("/index/test")
    public Object test(String deviceId,String appId){
        return deviceService.getActiveDeviceByDeviceIdAndAppIdRaw(deviceId, appId); 
    }
}