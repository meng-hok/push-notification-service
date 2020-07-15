package com.kosign.push.apps.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosign.push.utils.enums.KeyConfEnum;

@EqualsAndHashCode(callSuper=false)
@Data
public class ResponseListApp extends RequestAppIdentifier
{
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
    
    @JsonProperty("create_at")
    private Timestamp createdAt;
  
    @JsonProperty("create_by")
    private String createdBy;
    
    @JsonProperty("sts")
    private Character status = KeyConfEnum.Status.ACTIVE;
}
