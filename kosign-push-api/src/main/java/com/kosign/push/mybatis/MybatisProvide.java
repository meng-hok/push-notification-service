package com.kosign.push.mybatis;

import com.kosign.push.history.NotificationHistory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class MybatisProvide {

    public String insertHistory(@Param("history") NotificationHistory history) {
        System.out.println(history);
        String sql =   new SQL(){{
            INSERT_INTO("ps_history");
            INTO_COLUMNS("id, message, reciever_id, title, app_id, status, to_platform, response_msg");
            INTO_VALUES(history.getId().toString(),history.getMessage(),history.getRecieverId(),history.getTitle(),history.getAppId(),history.getStatus(),history.getToPlatform(),history.getResponseMsg());
        }}.toString();

        System.out.println(sql);

        return sql;
    }
}
