package com.kosign.push.devices.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestPushAgentAll {
    @Size(max  = 100)
    public String title;
    @Size(max  = 255)
    public String message;

}