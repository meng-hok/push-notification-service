package com.kosign.push.devices.dto;

import lombok.Data;

import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Data
public class RequestPushDevice extends RequestAgent{

    public ArrayList<String> deviceIdList;

    @JsonIgnore
    @Override
    public String getDevice_id() {
        // TODO Auto-generated method stub
        return super.getDevice_id();
    }
}
