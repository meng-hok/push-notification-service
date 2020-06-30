package com.kosign.push.utils.messages;
/*

 for response message
*/

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ApplicationResponse {

    private String id;

    private String name;

    private Integer totalPush=0;

    private Integer platform=0;

    private Integer ios=0;

    private Integer android=0;

    private Integer fcm=0;

    private Timestamp createdAt;
}
