package com.kosign.push.notificationHistory.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class RequestSearchHistory {
    @NotEmpty
    public String startDate;
    @NotEmpty
    public String endDate;
    public String msgTitle;
}