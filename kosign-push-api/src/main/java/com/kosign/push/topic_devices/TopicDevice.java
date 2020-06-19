package com.kosign.push.topic_devices;

import com.kosign.push.devices.Device;
import com.kosign.push.topics.Topic;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name="ps_topic_device")
public class TopicDevice {
/*
* Pre Table
*   PERSIST BY TOPIC MODEL
*   GET BY TOPICDEVICE MODEL
* */
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @CreationTimestamp
    private Timestamp createdAt;

    public TopicDevice( Topic topic,Device device) {
        this.topic = topic;
        this.device = device;
    }
}
