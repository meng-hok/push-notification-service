package com.kosign.push.notificationHistory;

import java.util.*;
import java.util.stream.*;

import com.github.pagehelper.*;
import com.kosign.push.apps.dto.*;
import com.kosign.push.devices.dto.*;
import com.kosign.push.notificationHistory.dto.RequestHistoryList;
import com.kosign.push.notificationHistory.dto.ResponseHistoryList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.plaf.synth.*;

@Service
public class NotificationHistoryService 
{
    @Autowired
    NotificationHistoryRepository historyRepo; 
    
    @Autowired
    NotificationHistoryBatisRepository historyBatisRepo; 

    private Logger logger = LoggerFactory.getLogger(NotificationHistoryService.class);

    public void saveHistory (NotificationHistoryEntity history)
    {
        historyRepo.save(history);
    }
    public void insertHistory(NotificationHistoryEntity history) 
    { 
        historyBatisRepo.insertHistory(history);
    }


    public void saveHistoryWithRabbit(NotificationHistoryEntity history)
    {
        logger.info("{ Response from History Queue  }");
        historyRepo.save(history);
    }

    public ResponseHistoryList  findNotificationHistoryById(Integer id)
    {
        return historyBatisRepo.findNotificationHistoryById(id);
    }

    public List<ResponseHistoryList>  findNotificationHistoryByBulkId(String bulkId)
    {
        return historyBatisRepo.findNotificationHistoryByBulkId(bulkId);
    }

    public  HashMap<String,Object> findAllHistories(RequestHistoryList request,int pageNum,int pageSize)
    {
        PageHelper.startPage(pageNum,pageSize,true);
        List<ResponseHistoryList> responseHis;
        PageInfo<ResponseHistoryList> pageInfos= PageHelper.startPage(pageNum,pageSize).doSelectPageInfo(()->historyBatisRepo.findAllHistories(request));
        responseHis=pageInfos.getList().stream().map(ResponseHistoryList::new).collect(Collectors.toList());
        PageInfoCustome pageInfoCustome=new PageInfoCustome(pageInfos);
        HashMap<String,Object> response= new HashMap<>();
        response.put("pagination",pageInfoCustome);
        response.put("datas",responseHis);
        return response;
    }
}