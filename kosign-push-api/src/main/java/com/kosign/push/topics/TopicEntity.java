package com.kosign.push.topics;

import com.kosign.push.apps.AppEntity;
import com.kosign.push.devices.DeviceEntity;
import com.kosign.push.utils.enums.AgentEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name ="ps_topic",uniqueConstraints={@UniqueConstraint(columnNames = {"name", "application_id","agent"})} )
//uniqueConstraints={
//@UniqueConstraint(columnNames = {"productId", "serial"}
@Entity
public class TopicEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private Character agent;

    @ManyToOne
    private AppEntity application;
    /*
    * Topic Device created Middle table
    * */

    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "topic_id"),
            inverseJoinColumns = @JoinColumn(name = "device_id")
    )
//    @OneToMany(mappedBy = "topic")
    private List<DeviceEntity> device;


    private Character status='1';
    @CreationTimestamp
    private Timestamp timestamp;

    public TopicEntity(String name, AppEntity application) {
        this.name = name;
        this.application = application;
    }
    public void setApns(){
        this.agent = AgentEnum.Agent.APNS;
    }
    public void setFcm(){
        this.agent = AgentEnum.Agent.FCM;
    }

//    public Topic(String name, Application application) {
//    }
}
