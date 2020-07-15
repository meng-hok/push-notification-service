package com.kosign.push.users;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.kosign.push.users.UserDetail;
import com.kosign.push.users.UserEntity;
import com.kosign.push.utils.FileStorageUtil;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.utils.enums.PlatformEnum;
import com.kosign.push.utils.enums.ResponseEnum;
import com.kosign.push.platformSetting.dto.APNS;
import com.kosign.push.platformSetting.dto.RequestCreateApns;
import com.kosign.push.platformSetting.dto.RequestCreateFcm;
import com.kosign.push.platformSetting.dto.RequestRemoveApns;
import com.kosign.push.platformSetting.dto.RequestRemoveFcm;
import com.kosign.push.platformSetting.dto.RequestUpdateFcm;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "Users")
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    public UserService userService;

    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PostMapping("/create/request")
    public Object  create(String username,String password) throws Exception{
        UserEntity user = userService.saveUserToRequestStatus(username, password);
        return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }
   
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping(value="/{userId}/approval")
    public Object approval(@PathVariable("userId") String userId) throws Exception{
         userService.approveUser(userId);
        return Response.getSuccessResponseNonDataBody(ResponseEnum.Message.SUCCESS);
    }
}