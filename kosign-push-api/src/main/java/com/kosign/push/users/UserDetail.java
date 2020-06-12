package com.kosign.push.users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetail implements UserDetails {

    /**
     *
     */
    private final User user;
 
    public UserDetail(final User user) {
        this.user = user;
    }

    private static final long serialVersionUID = 1L;

 
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
       
        list.add(new SimpleGrantedAuthority("ROLE_"+ user.getRole() ));

        return list;

    }


    public String getId(){
        return user.getId();
    }

    @Override
    public String getPassword() {
        
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        
        return true;
    }

    @Override
    public boolean isEnabled() {
       
        switch ( user.getStatus()) {
            case '1':
                
                return true;
           
            case '9':
                
                return false;
        
            case '0':
                return false;
        
            default:
                return false;
        }
    }
    
}