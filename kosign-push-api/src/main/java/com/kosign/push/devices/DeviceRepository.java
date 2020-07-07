package com.kosign.push.devices;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device,String> {

	List<Device> findByAppIdAndStatus(@Param("appId")String appId,@Param("status")Character status);

	List<Device> findByAppIdAndPlatformIdAndStatus(@Param("appId")String appId,@Param("platFormId") String platFormId,@Param("status") Character status);

    // @Query(nativeQuery = true, value = "SELECT * FROM ps_device_client WHERE device_id=:deviceId AND  app_id=:appId ")
	// Device findCByDeviceIdAndAppId(@Param("deviceId") String deviceId,@Param("appId") Character appId);
    
    // @Query(nativeQuery = true, value = "SELECT * FROM ps_device_client WHERE device_id=:deviceId AND  app_id=:appId LIMIT 1")
    // Device findByDeviceIdAndAppId(@Param("deviceId")String deviceId,@Param("appId") String appId);
       
    

	

}