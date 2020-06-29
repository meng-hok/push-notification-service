package com.kosign.push.apps;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppRepository extends JpaRepository<Application,String>{

	public List<Application> findByStatus(Character status);

	public List<Application> findByUserIdAndStatus(@Param("userId")String userId,@Param("active")Character active);


	@Query(nativeQuery = true ,value = "SELECT user_id FROM ps_application WHERE id = :appId AND status = :status LIMIT 1")
	public String findUserIdByAppId(@Param("appId")String appId,@Param("status")Character status);

	public Application findByIdAndStatus(String appId, Character status);

	// public List<Application> findByProjectIdAndStatus(String id, Character aCTIVE);
    

    
}