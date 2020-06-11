package com.kosign.push.users;

import java.util.Arrays;

import com.kosign.push.utils.KeyConf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    public User saveUserToRequestStatus(String username,String password){
        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password)); 
        user.setRole("USER");
        user.setStatus(KeyConf.Status.REQUESTING);
        return userRepo.save(user);
    }
   
    public User approveUser(String userId){
        User user = userRepo.getOne(userId);
     
        user.setStatus(KeyConf.Status.ACTIVE);
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) 
    {
        User user = userRepo.findByUsernameAndStatus(username, KeyConf.Status.ACTIVE);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }else{
            /**
             * for customize role 
            */
            // String []roles = user.getRole().split(",");            
            // SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER")
            return new UserDetail(user.getUsername(),user.getPassword(), Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
        }
        
    }

}