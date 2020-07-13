package com.kosign.push.notificationHistory;

import java.util.List;

import com.kosign.push.notificationHistory.dto.RequestHistoryList;
import com.kosign.push.notificationHistory.dto.ResponseHistoryList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public List<ResponseHistoryList> findAllHistories(RequestHistoryList request)
    {
        return historyBatisRepo.findAllHistories(request);
    }
}