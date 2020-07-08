package com.kosign.push.apps;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.kosign.push.users.User;
import com.kosign.push.utils.GlobalMethod;
import com.kosign.push.utils.KeyConf;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import lombok.Data;

@Data
@Table(name ="ps_application",uniqueConstraints={
        @UniqueConstraint(columnNames = {"user_id", "name"})}
)
@Entity
public class Application {
    // @GeneratedValue(strategy = GenerationType.AUTO )
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    @JsonProperty("created_by")
    @ManyToOne(optional = true)
    private User user;
    
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    private Character status = KeyConf.Status.ACTIVE;

    private String updatedBy;

    public Application(){}

	public Application(String  id ) {
		this.id = id;
	
    }
    @PrePersist()
    public void onPrepersist() {
        String userId = GlobalMethod.getUserCredential().getId();
        this.updatedBy = userId;
        if(this.user == null ){
            this.user = new User();
        }
        user.setId(userId);
    }
    @PreUpdate
    public void onPreUpdate() {
        this.updatedBy = GlobalMethod.getUserCredential().getId();
    }
      
    
}
