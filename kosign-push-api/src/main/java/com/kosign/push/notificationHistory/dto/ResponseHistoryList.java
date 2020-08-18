package com.kosign.push.notificationHistory.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.Data;

@Data
public class ResponseHistoryList 
{
    private Integer id;
    private String reciever_id;
    private String title;
    private String message;
    private String to_platform;
    // @JsonRawValue
    private String response_msg;
    private Integer count;
    private Timestamp created_at;
    private String status;
    
}
