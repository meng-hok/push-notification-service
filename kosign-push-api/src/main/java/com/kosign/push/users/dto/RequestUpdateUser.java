package com.kosign.push.users.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RequestUpdateUser {
    public String name;
    public String company;
    public String password;
    public String previousPassword;
}
