package com.kosign.push.utils.messages;

import lombok.Data;

@Data
public class AgentIdentifier {
   
    public String device_id;
  
    public String app_id;


    public String platform_id;

    public String token;
}