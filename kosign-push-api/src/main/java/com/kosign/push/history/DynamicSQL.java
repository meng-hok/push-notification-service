package com.kosign.push.history;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

public class DynamicSQL {

    public String getSQL(@Param("startDate") String startDate ,@Param("endDate") String endDate,@Param("msgTitle") String msgTitle)
    {
        String sql="SELECT  ph.id,ph.reciever_id,ph.title,ph.message,ph.to_platform,ph.status,ph.response_msg,ph.created_at,ph.count FROM  ps_history ph \n"+
        "WHERE ph.created_at >= CONCAT(#{startDate},' 00:00:00')::TIMESTAMP AND \n"+
        " ph.created_at < CONCAT(#{endDate},' 23:59:59'):: TIMESTAMP";
        if(StringUtils.isNotBlank(msgTitle))
        {
            sql += " AND LOWER(ph.title) LIKE '%' || LOWER(#{msgTitle}) || '%'";
        }
        return sql;
    }
    
}