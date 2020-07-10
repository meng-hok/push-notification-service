package com.kosign.push.devices.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@AllArgsConstructor
@Data
@ToString
//@Entity
public class Agent extends AgentBody {

    // public String id;
   

    public String authorized_key;
  
    public String pfilename;
    public String team_id;
    public String file_key;
    public String bundle_id;



   
  

    public Agent(){}
}