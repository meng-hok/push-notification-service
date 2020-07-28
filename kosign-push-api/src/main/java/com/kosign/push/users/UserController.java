package com.kosign.push.users;

import javax.transaction.Transactional;

import com.kosign.push.users.dto.RequestUpdateUser;
import com.kosign.push.utils.messages.Response;
import io.swagger.annotations.Api;
import org.omg.CORBA.RepositoryIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.kosign.push.utils.GlobalMethod;
@PreAuthorize("#oauth2.hasScope('READ')")
@Api(tags = "Users")
@RestController
@RequestMapping("/api/v1")
public class UserController 
{
    @Autowired
    public UserService userService;
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PostMapping("/account")
    public Object createAccount(
		@RequestParam String username,
		@RequestParam String password
    ) throws Exception
    {
        UserEntity user = userService.saveUser(username, password);
        return Response.setResponseEntity(HttpStatus.OK, user);
    }
   
    // @Transactional(rollbackOn = Exception.class)
    // @ResponseBody
    // @PreAuthorize("hasRole('ROLE_OPERATOR')")
    // @PostMapping(value="/account/verify/{userId}")
    public Object verifyAccount(@PathVariable("userId") String userId) throws Exception
    {
        userService.approveUser(userId);
        return Response.setResponseEntity(HttpStatus.OK);
    }
   
    @GetMapping("/account")
    public Object getInformation(){

      try{
          UserEntity userEntity = userService.getActiveUser(GlobalMethod.getUserCredential().getId());
          userEntity.setPassword("");
          return Response.setResponseEntity(HttpStatus.OK,userEntity);
      }catch (Exception ex) {
          return Response.setResponseEntity(HttpStatus.BAD_REQUEST,ex.getMessage());
      }
    }

    @PutMapping("/account")
    public Object updateInformation(
            @RequestBody RequestUpdateUser user
            ){
        try {
            UserEntity userEntity =  userService.updateUser(user);

            return Response.setResponseEntity(HttpStatus.OK,userEntity);
        }catch (Exception ex) {
            return Response.setResponseEntity(HttpStatus.BAD_REQUEST,ex.getMessage());
        }

    }
}