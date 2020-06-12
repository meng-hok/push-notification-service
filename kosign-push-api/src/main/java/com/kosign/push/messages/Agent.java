package com.kosign.push.messages;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
public class Agent {
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