package com.kosign.push.platformSetting.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.kosign.push.utils.aspects.ApplicationAspect;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class RequestCreateApns extends ApplicationAspect{
    @NotNull
    @NotEmpty
    public String team_id;
    @NotNull
    @NotEmpty
    public String file_key;
    @NotNull
    @NotEmpty
    public String bundle_id;

 
}