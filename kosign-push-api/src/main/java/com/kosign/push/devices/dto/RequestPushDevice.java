package com.kosign.push.devices.dto;

import lombok.Data;

import java.util.ArrayList;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
public class RequestPushDevice{
    @Size(max  = 100)
    public String title;
    @Size(max  = 255)
    public String message;
    
    @NotNull
    @JsonProperty("device_id_list")
    public ArrayList<String> deviceIdList;
}
