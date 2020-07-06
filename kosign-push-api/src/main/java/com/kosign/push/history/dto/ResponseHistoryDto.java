package com.kosign.push.history.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ResponseHistoryDto 
{
    private Integer id;
    private String reciever_id;
    private String title;
    private String message;
    private String to_platform;
    private String response_msg;
    private Integer count;
    private Timestamp created_at;
    
}
