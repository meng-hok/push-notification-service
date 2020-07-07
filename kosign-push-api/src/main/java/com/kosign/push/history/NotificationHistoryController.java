package com.kosign.push.history;

import java.util.List;


import com.kosign.push.history.dto.ResponseHistoryDto;
import com.kosign.push.utils.KeyConf;
import com.kosign.push.utils.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

// @RestController
// @PreAuthorize("#oauth2.hasScope('READ')")
public class NotificationHistoryController {
    
    @Autowired
    private NotificationHistoryService histoService;
    @GetMapping("/api/public/history/{id}")
    public Object displayHistory(@PathVariable("id")Integer id){
     ResponseHistoryDto notiHisto= histoService.getPushNotificationHistoryById(id);
     return Response.getResponseBody(KeyConf.Message.SUCCESS,notiHisto, true);
    }

    @PostMapping("/api/public/history")
    public Object send(String startDate,String endDate,String msgTitle) {

        List<ResponseHistoryDto> listHis = histoService.getAllHistory(startDate, endDate, msgTitle);
        
        return Response.getResponseBody(KeyConf.Message.SUCCESS,listHis , true);
    }

 
       
}