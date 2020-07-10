package com.kosign.push.platformSetting;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PlatformSettingRepository extends JpaRepository<PlatformSettingEntity,String>{

	public List<PlatformSettingEntity> findByApplicationIdAndStatus(String appId, Character status);

	public PlatformSettingEntity findByApplicationIdAndPlatformIdAndStatus(String appId, String platformId, Character status);

	@Query(nativeQuery=true,value = "SELECT authorized_key FROM ps_platform_setting WHERE application_id = :appId AND platform_id = :platformId AND status = :status LIMIT 1 ")
	public String findAuthorizedKeyByAppIdAndPlatFormRaw(@Param("appId") String appId,@Param("platformId") String platformId,@Param("status") Character status);
}