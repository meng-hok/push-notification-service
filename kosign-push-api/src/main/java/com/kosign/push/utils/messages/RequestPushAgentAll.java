package com.kosign.push.utils.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class RequestPushAgentAll extends RequestAgent {

    @JsonIgnore
    public String getDevice_id() {
        // TODO Auto-generated method stub
        return super.getDevice_id();
    }
}