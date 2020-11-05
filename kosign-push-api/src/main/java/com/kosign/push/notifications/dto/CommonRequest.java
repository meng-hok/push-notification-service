package com.kosign.push.notifications.dto;

import lombok.*;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CommonRequest {
    private String appId;
    private String title;
    private String message;
    private Integer badgeCount;
    private String bulkId;
    private String image;
}
