package com.kosign.push.devices;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import com.kosign.push.devices.dto.RequestDeviceList;

public class DeviceBatisDynasmicSql 
{
	public String getSQL(@Param("request") RequestDeviceList request)
	{
		String sql = "/*DEVICE_CLIENT_R001*/\nSELECT dc.token as push_id\n    ,  dc.model_name\n  ,  (\n      SELECT pl.code \n        FROM ps_platform pl \n     WHERE pl.id = dc.platform_id\n      ) as plat_code\n  ,  dc.os as os_version\n  ,  dc.created_at\n  FROM ps_device_client dc\n WHERE dc.app_id = #{request.appId}\n   AND dc.status = '1'\n   AND dc.created_at >= CONCAT(#{request.startDate},' 00:00:00')::TIMESTAMP\n   AND dc.created_at <= CONCAT(#{request.endDate}  ,' 23:59:59')::TIMESTAMP";
		if(StringUtils.isNotBlank(request.getPushId()))
		{
			sql += " AND dc.token = #{request.pushId}";
		}
		
		if(StringUtils.isNotBlank(request.getModelName()))
		{
			sql += " AND LOWER(dc.model_name) LIKE '%' || LOWER(#{request.modelName}) || '%'";
		}
		
		if(StringUtils.isNotBlank(request.getPlatCode()))
		{
			sql += " AND LOWER((SELECT pl.code FROM ps_platform pl WHERE pl.id = dc.platform_id ) ) LIKE '%' || LOWER(#{request.platCode}) || '%'";
		}
		
		if(StringUtils.isNotBlank(request.getOsVersion()))
		{
			sql += " AND LOWER(dc.os) LIKE '%' || LOWER(#{request.osVersion}) || '%'";
		}
		
		return sql;
	}
	
}