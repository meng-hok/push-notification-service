package com.kosign.push.utils.aspects;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ApplicationAspect 
{
    @NotEmpty
    private String app_id;
}