package com.kosign.push.users;

import com.kosign.push.users.dto.RequestUpdateUser;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.enums.KeyConfEnum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    
    @Autowired
    UserRepository userRepo;

    @Autowired
    PasswordEncoder encoder;

    public UserEntity saveUserToRequestStatus(String username,String password){
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encoder.encode(password)); 
        user.setRole("USER");
        user.setStatus(KeyConfEnum.Status.REQUESTING);
        return userRepo.save(user);
    }

    public UserEntity saveUser(String username,String password){
        UserEntity user = new UserEntity();
        user.setUsername(username);
        user.setPassword(encoder.encode(password)); 
        user.setRole("USER");
        user.setStatus(KeyConfEnum.Status.ACTIVE);
        return userRepo.save(user);
    }
   
    public UserEntity approveUser(String userId){
        UserEntity user = userRepo.getOne(userId);
     
        user.setStatus(KeyConfEnum.Status.ACTIVE);
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) 
    {
        UserEntity user = userRepo.findByUsernameAndStatus(username, KeyConfEnum.Status.ACTIVE);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }else{
            /**
             * for customize role 
            */
            // String []roles = user.getRole().split(",");            
            // SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority("ROLE_USER")
            return new UserDetail(user);
        }
        
    }

    public UserEntity updateUser(RequestUpdateUser user) throws  Exception {
       UserEntity userEntity =  getActiveUser(GlobalMethod.getUserCredential().getId());
       if(!StringUtils.isEmpty(user.getPreviousPassword())) {
           if(!encoder.matches(user.getPreviousPassword(),userEntity.getPassword()))
               throw new Exception();
           userEntity.setPassword(encoder.encode(user.getPassword()));
       }

       userEntity.setName(user.getName());
       userEntity.setCompany(user.getCompany());
       return userRepo.save(userEntity);
    }

    public UserEntity getActiveUser(String userId) throws Exception {
        Optional<UserEntity> userEntity =  userRepo.findById(userId);
        if(KeyConfEnum.Status.DISABLED.equals(userEntity.get().getStatus()))
            return null;
        return userEntity.get();
    }

    public Page<UserEntity> getAllUsers(Pageable pageable) throws Exception { 
        return userRepo.findAll(pageable);
    }
}