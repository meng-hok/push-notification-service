package com.kosign.push.devices.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPushAgentAll {
    public String title;
    public String message;

}