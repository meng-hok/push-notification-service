package com.kosign.push.users;

import javax.transaction.Transactional;
import com.kosign.push.utils.messages.Response;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Users")
@RestController
@RequestMapping("/api/v1")
public class UserController 
{
    @Autowired
    public UserService userService;

    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PostMapping("/account")
    public Object createAccount(
		@RequestParam String username,
		@RequestParam String password
    ) throws Exception
    {
        UserEntity user = userService.saveUserToRequestStatus(username, password);
        return Response.setResponseEntity(HttpStatus.OK, user);
    }
   
    @Transactional(rollbackOn = Exception.class)
    @ResponseBody
    @PreAuthorize("hasRole('ROLE_OPERATOR')")
    @PostMapping(value="/account/verify/{userId}")
    public Object verifyAccount(@PathVariable("userId") String userId) throws Exception
    {
        userService.approveUser(userId);
        return Response.setResponseEntity(HttpStatus.OK);
    }
}