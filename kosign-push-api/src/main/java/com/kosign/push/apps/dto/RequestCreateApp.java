package com.kosign.push.apps.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class RequestCreateApp 
{
    @NotEmpty(message = "application name can not be empty")
    public String name;
}