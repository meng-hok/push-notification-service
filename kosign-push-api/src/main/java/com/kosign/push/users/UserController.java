package com.kosign.push.users;

import javax.transaction.Transactional;

import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/api/public/users")
public class UserController {
    
    @Autowired
    UserService userService;
  
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PostMapping("/create/request")
    public Object  create(String username,String password) throws Exception{
        
        return Response.getResponseBody(KeyConf.Message.SUCCESS ,userService.saveUserToRequestStatus(username, password),true);
    }
   
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping(value="/{userId}/approval")
    public Object approval(@PathVariable("userId") String userId) throws Exception{
        return userService.approveUser(userId);
    }
    
}