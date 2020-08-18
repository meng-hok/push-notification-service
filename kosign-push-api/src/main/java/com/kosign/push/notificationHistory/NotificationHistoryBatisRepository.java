package com.kosign.push.notificationHistory;

import java.util.List;

import com.kosign.push.notificationHistory.dto.RequestHistoryList;
import com.kosign.push.notificationHistory.dto.ResponseHistoryList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

@Repository()
public interface NotificationHistoryBatisRepository
{
    @SelectProvider(method = "getSQL", type = NotificationHistoryBatisDynamicSql.class)
    public List<ResponseHistoryList> findAllHistories(@Param("request") RequestHistoryList request);

    @Select("SELECT  ph.id,ph.reciever_id,ph.title,ph.message,ph.to_platform,ph.status,ph.response_msg,ph.created_at,ph.count FROM  ps_history ph \n"+
    "WHERE ph.id=#{id}")
    public ResponseHistoryList findNotificationHistoryById(@Param("id") Integer id);
  
    @Insert("INSERT INTO ps_history \n" +
        "(message, reciever_id, title, app_id, status, to_platform, response_msg,count,bulk_id) VALUES  \n"+
        "(#{history.message},#{history.recieverId},#{history.title},#{history.appId},#{history.status},#{history.toPlatform},#{history.responseMsg},#{history.count},#{history.bulkId})")
    Integer insertHistory(@Param("history") NotificationHistoryEntity history);

    @Select("SELECT  ph.id,ph.reciever_id,ph.title,ph.message,ph.to_platform,ph.status,ph.response_msg,ph.created_at,ph.count FROM  ps_history ph \n"+
            "WHERE ph.bulk_id=#{bulkId}")
    public List<ResponseHistoryList> findNotificationHistoryByBulkId(@Param("bulkId")String bulkId);
}

