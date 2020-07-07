package com.kosign.push.devices;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

	List<Device> findByAppIdAndStatus(@Param("appId")String appId,@Param("status")Character status);

	List<Device> findByAppIdAndPlatformIdAndStatus(@Param("appId")String appId,@Param("platFormId") String platFormId,@Param("status") Character status);

    @Query(nativeQuery = true, value = "SELECT * FROM ps_device_client WHERE device_id=:deviceId AND  app_id=:appId ")
	Device findCByDeviceIdAndAppId(@Param("deviceId") String deviceId,@Param("appId") Character appId);
    
    @Query(nativeQuery = true, value = "SELECT * FROM ps_device_client WHERE device_id=:deviceId AND  app_id=:appId LIMIT 1")
    Device findByDeviceIdAndAppId(@Param("deviceId")String deviceId,@Param("appId") String appId);
        //SELECT id ,token , status , platform ,app_id ,device_id FROM ps_device_client WHERE device_id=:deviceId AND  app_id=:appId 
    // public List<Device> findByUserIdAndStatus(String userId,Character Status);
    // /**
    // * Using native query instead of joining 
    // */
    // @Query(nativeQuery = true, value = "SELECT * FROM device WHERE user_id = :userId AND appId = :appId AND status = :active" )
	// public List<Device> findByUserIdAndAppIdAndStatus(String userId, String appId, Character active);

    @Query(nativeQuery = true, value = "SELECT  d.device_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n"+
                                        "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n"+
                                        "ON d.platform_id = p_s.platform_id \n"+
                                        "WHERE d.device_id = :deviceId AND app_id = :appId AND d.status = '1' LIMIT 1")
    List<Map<String,String>> findByDeviceIdAndAppIdRaw(@Param("deviceId")String deviceId,@Param("appId") String appId);
   
    @Query(nativeQuery = true, value = "SELECT  d.user_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n"+
                                        "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n"+
                                        "ON d.platform_id = p_s.id \n"+
                                        "WHERE d.user_id = :userId AND app_id = :appId AND d.status = '1' LIMIT 1")   
    List<Map<String, String>> findByUserIdAndAppIdRaw(@Param("userId")String userId,@Param("appId") String appId);

    @Query(nativeQuery = true, value = "SELECT  d.device_id , d.token,d.app_id,d.platform_id, p_s.authorized_key , p_s.bundle_id , p_s.key_id as file_key, p_s.team_id ,p_s.push_url as pFileName \n"+
                                        "FROM ps_device_client d INNER JOIN ps_platform_setting p_s \n"+
                                        "ON d.platform_id = p_s.id \n"+
                                        "WHERE  app_id = :appId AND d.status = '1'")
    List<Map<String, String>> findByAppIdRaw(@Param("appId")String appId);
    
}