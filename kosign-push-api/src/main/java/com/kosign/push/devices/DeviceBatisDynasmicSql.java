package com.kosign.push.devices;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;


public class DeviceBatisDynasmicSql {
    public String getSQL(@Param("app_id") String appId,@Param("start_date") String startDate, @Param("end_date") String endDate, @Param("push_id") String token, @Param("model_name") String modelName,
    @Param("plat_code") String platform, @Param("os_version") String os){
        String sql= "select dc.token as push_id , dc.model_name, \n" 
		+"(select pl.code from ps_platform pl where pl.id = dc.platform_id ) as plat_code, \n"
		+"dc.os as os_version, \n"
		+"dc.created_at \n"
	    +"from ps_device_client dc \n"
	    +"where dc.app_id = #{app_id} AND  dc.status = '1' and dc.created_at >= CONCAT(#{start_date},' 00:00:00')::TIMESTAMP AND \n"
        +"dc.created_at <= CONCAT(#{end_date},' 23:59:59'):: TIMESTAMP";
        if(StringUtils.isNotBlank(token)){
            sql+=" AND dc.token LIKE  '%' || #{push_id} || '%'";
        }
        if(StringUtils.isNotBlank(modelName)){
            sql+=" AND LOWER(dc.model_name) LIKE '%' || LOWER(#{model_name}) || '%'";
        }
        if(StringUtils.isNotBlank(platform)){
            sql+=" AND LOWER((select pl.code from ps_platform pl where pl.id = dc.platform_id ) ) LIKE '%' || LOWER(#{plat_code}) || '%'";
        }
        if(StringUtils.isNotBlank(os)){
            sql+=" AND LOWER(dc.os) LIKE '%' || LOWER(#{os_version}) || '%'";
        }
        return sql;
    }
}