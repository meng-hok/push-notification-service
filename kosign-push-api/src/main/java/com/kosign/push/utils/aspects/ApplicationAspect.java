package com.kosign.push.utils.aspects;

import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ApplicationAspect 
{
    @JsonProperty("app_id")
    @NotEmpty
    private String appId;
}