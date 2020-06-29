package com.kosign.push.apps;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kosign.push.users.User;

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

    @JsonIgnore
    @ManyToOne(optional = true)
    private User user;
    
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    private Character status = '1';

    public Application(){}

	public Application(String  id ) {
		this.id = id;
	
	}
    
}
