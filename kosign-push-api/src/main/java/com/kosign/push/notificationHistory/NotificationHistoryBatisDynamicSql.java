package com.kosign.push.notificationHistory;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;

import com.kosign.push.notificationHistory.dto.RequestHistoryList;

public class NotificationHistoryBatisDynamicSql
{
    public String getSQL(@Param("request") RequestHistoryList request)
    {
        String sql="SELECT  ph.id,ph.reciever_id,ph.title,ph.message,ph.to_platform,ph.status,ph.response_msg,ph.created_at,ph.count FROM  ps_history ph \n"+
        "WHERE ph.app_id = #{request.appId} AND ph.created_at >= CONCAT(#{request.startDate},' 00:00:00')::TIMESTAMP AND \n"+
        " ph.created_at < CONCAT(#{request.endDate},' 23:59:59'):: TIMESTAMP";
        if(StringUtils.isNotBlank(request.getMsgTitle()) && StringUtils.isNotBlank(request.getReceiverId()))
        {
            sql += " AND LOWER(ph.title) LIKE '%' || LOWER(#{request.msgTitle}) || '%' AND LOWER(ph.reciever_id) LIKE '%' || LOWER(#{request.receiverId}) || '%' ";
        }
        else if(StringUtils.isNotBlank(request.getReceiverId())){
            sql += " AND LOWER(ph.reciever_id) LIKE '%' || LOWER(#{request.receiverId}) || '%'";

        }
        else if(StringUtils.isNotBlank(request.getMsgTitle())){
            sql += " AND LOWER(ph.title) LIKE '%' || LOWER(#{request.msgTitle}) || '%' ";
        }
        return sql;
    }
    
}