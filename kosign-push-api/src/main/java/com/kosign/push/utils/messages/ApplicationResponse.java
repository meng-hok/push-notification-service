package com.kosign.push.utils.messages;
/*

 for response message
*/

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ApplicationResponse {

    protected String id;

    protected String name;

    protected Integer totalPush=0;

    protected Integer platform=0;

    protected Integer ios=0;

    protected Integer android=0;

    protected Integer fcm=0;

    protected Timestamp createdAt;
}
