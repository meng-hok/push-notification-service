package com.kosign.push.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestUser {
    public String username;
    public String password;
    public String name;
    public String company;
}
