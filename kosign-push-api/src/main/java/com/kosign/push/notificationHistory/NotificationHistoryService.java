package com.kosign.push.notificationHistory;

import java.util.List;

import com.kosign.push.notificationHistory.dto.ResponseHistoryDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationHistoryService {
    
    @Autowired
    NotificationHistoryRepository historyRepo; 
    @Autowired
    NotificationHistoryBatisRepository historyBatisRepo; 

    private Logger logger = LoggerFactory.getLogger(NotificationHistoryService.class);

    public void saveHistory (NotificationHistory history){

        historyRepo.save(history);
    }
    public void insertHistory(NotificationHistory history) { 
        historyBatisRepo.insertHistory(history);
    }


    public void saveHistoryWithRabbit(NotificationHistory history){
        logger.info("{ Response from History Queue  }");
        historyRepo.save(history);
    }

    public ResponseHistoryDto  getPushNotificationHistoryById(Integer id){
        return historyBatisRepo.findAllHistoryById(id);
    }

    public List<ResponseHistoryDto> getAllHistory(String startDate,String endDate,String msgTitle)
    {
        return historyBatisRepo.findAllHistory(startDate, endDate, msgTitle);
    }
}