package com.kosign.push.notificationHistory.dto;

import lombok.Data;

@Data
public class RequestHistoryList 
{
	private String appId;
    private String startDate;
    private String endDate;
    private String msgTitle;
    private String receiverId;
}