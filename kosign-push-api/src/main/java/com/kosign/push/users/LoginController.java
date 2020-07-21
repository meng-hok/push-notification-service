package com.kosign.push.users;

import javax.servlet.http.HttpServletRequest;

import com.kosign.push.utils.enums.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    

    @Autowired
    UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private HttpServletRequest request;


    @GetMapping("/signin")
    public String login(@RequestParam(required = false , value = "err_msg" ,defaultValue = "" ) String errMsg,Model model){
        String msg = errMsg;
        model.addAttribute("MSG",msg);
        return "users/login";
    }
    @PostMapping("/signin")
    public String login(String username,String password) {
        try {
            UserDetail userDetail = (UserDetail) userService.loadUserByUsername(username);
            Boolean status = passwordEncoder.matches(password, userDetail.getPassword());

            if(!status )
                return "redirect:/signin?err_msg=* Incorrect Password";

            if(!RoleEnum.Role.Developer.equals(userDetail.getRole()))
                return "redirect:/signin?err_msg=* Permission Denied";

            request.getSession().setAttribute("user", userDetail);
            return "redirect:/swagger-ui.html";
        } catch (Exception e) {
            return "redirect:/swagger-ui.html?err_msg=* Invalid Credential";
        }
    }
    @RequestMapping(value = "/swagger/logout" ,method = {RequestMethod.POST,RequestMethod.GET})
    public String logout(){
        request.getSession().setAttribute("user", null);
        return "redirect:/signin";
    }
}