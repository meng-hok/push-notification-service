package com.kosign.push.utils.messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import javax.persistence.Id;

@AllArgsConstructor
@Data
@ToString
//@Entity
public class Agent {
    @Id
    public String id;
    public String app_id;
    public String platform_id;
    public String device_id;

    public String authorized_key;
  
    public String pfilename;
    public String team_id;
    public String file_key;
    public String bundle_id;

    public String token;
   
    public String title;
    public String message;

    public Agent(){}
}