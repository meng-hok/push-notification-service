package com.kosign.push.apps.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RequestAppIdentifier 
{
    @JsonProperty("app_id")
    public String id;
    @JsonProperty("app_name")
    public String name;

}