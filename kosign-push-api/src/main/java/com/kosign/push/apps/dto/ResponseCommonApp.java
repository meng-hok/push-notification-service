package com.kosign.push.apps.dto;

import com.kosign.push.apps.AppEntity;

public class ResponseCommonApp extends AppEntity 
{
    public ResponseCommonApp(AppEntity application) 
    {
        this.id = application.getId();
        this.name = application.getName();
        this.user = application.getUser();
        this.createdAt = application.getCreatedAt();
        this.updatedAt = application.getUpdatedAt();
        this.status = application.getStatus();
        this.updatedBy = application.getUpdatedBy();
    }
}