package com.kosign.push.notifications.dto;

import lombok.*;

import java.util.*;

@ToString
@NoArgsConstructor
//@AllArgsConstructor
@Getter
@Setter
public class CommonRequest {
    private String appId;
    private String title;
    private String message;
    private Integer badgeCount;
    private String bulkId;
    private String image;
    private Map actionType;
    public CommonRequest(String appId,String title,String message,Integer badgeCount,String bulkId,String image,Map actionType){
        this.appId=appId;
        this.title=title;
        this.message=message;
        this.badgeCount=badgeCount;
        this.bulkId=bulkId;
        this.image=image;
        this.actionType=actionType;
    }
    public CommonRequest(String appId,String title,String message,Integer badgeCount,String bulkId,String image){
        this.appId=appId;
        this.title=title;
        this.message=message;
        this.badgeCount=badgeCount;
        this.bulkId=bulkId;
        this.image=image;
    }
}
