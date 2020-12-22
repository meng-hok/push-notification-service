package com.kosign.push.notificationHistory.dto;

import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonRawValue;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
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

    public ResponseHistoryList() {
    }

    public ResponseHistoryList(ResponseHistoryList res) {
        this.id= res.getId();
        this.reciever_id= res.getReciever_id();
        this.title=res.getTitle();
        this.message=res.getMessage();
        this.to_platform=res.getTo_platform();
        this.count=res.getCount();
        this.created_at=res.getCreated_at();
        this.status=res.getStatus();
        this.response_msg=res.getResponse_msg();
    }
}
