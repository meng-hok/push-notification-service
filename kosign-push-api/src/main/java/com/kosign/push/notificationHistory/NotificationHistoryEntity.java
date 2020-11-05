
package com.kosign.push.notificationHistory;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Data;

@Data
@Table(name ="ps_history")
@Entity
public class NotificationHistoryEntity 
{
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Id
    private Integer id;
    private String appId;
    private String recieverId;
    private String title;
    private String message;
    private String image;
    private Integer badgeCount;
    @Column(length = 8)
    private String toPlatform;
    @Column(length = 8)
    private String status;
    @Column(length = 1000)
    private String responseMsg;
    private Integer count=1;
    private String bulkId;

    @ColumnDefault(value = "now()")
    @CreationTimestamp
    private Timestamp createdAt;

    public NotificationHistoryEntity(String appId, String recieverId, String title, String message) 
    {
        this.appId = appId;
        this.recieverId = recieverId;
        this.title = title;
        this.message = message;
    }

    public NotificationHistoryEntity(){}

    public NotificationHistoryEntity(String appId, String recieverId, String title, String message, String toPlatform,
            String status,String responseMsg) 
    {
        this.appId = appId;
        this.recieverId = recieverId;
        this.title = title;
        this.message = message;
        this.toPlatform = toPlatform;
        this.status = status;
        this.responseMsg = responseMsg;
    }

    @Builder
    public NotificationHistoryEntity(Integer id, String appId, String recieverId, String title, String message,String image, Integer badgeCount, String toPlatform, String status, String responseMsg, Integer count, String bulkId, Timestamp createdAt) {
        this.id = id;
        this.appId = appId;
        this.recieverId = recieverId;
        this.title = title;
        this.message = message;
        this.badgeCount = badgeCount;
        this.image = image;
        this.toPlatform = toPlatform;
        this.status = status;
        this.responseMsg = responseMsg;
        this.count = count;
        this.bulkId = bulkId;
        this.createdAt = createdAt;
    }
}