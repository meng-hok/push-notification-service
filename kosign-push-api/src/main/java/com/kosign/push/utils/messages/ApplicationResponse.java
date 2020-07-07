package com.kosign.push.utils.messages;
/*

 for response message
*/

import lombok.Data;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class ApplicationResponse {

    protected String id;

    protected String name;

    @JsonProperty("subs_cnt")
    private Integer subscriber=0;

    @JsonProperty("plat_cnt")
    private Integer platform=0;

    @JsonProperty("ttl_push_apns_cnt")
    private Integer apns=0;
  
    @JsonProperty("ttl_push_fcm_cnt")
    private Integer fcm=0;
    
    @JsonProperty("ttl_push")
    private Integer totalPush=0;
  
    @JsonIgnore
    private Integer ios=0;
    @JsonIgnore
    private Integer android=0;
    @JsonIgnore
    private Integer web=0;
    
    @JsonProperty("created_at")
    private Timestamp createdAt;
  
    @JsonProperty("created_by")
    private String createdBy;

    private Character status = '1';
}
