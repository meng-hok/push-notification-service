package com.kosign.push.devices.dto;

import lombok.Data;

@Data
public class RequestAgent {
    public String title;
    public String message;
    public String device_id; 
    public String app_id;
}