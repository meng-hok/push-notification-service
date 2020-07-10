package com.kosign.push.notificationHistory;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;


import com.kosign.push.publics.SuperController;

import com.kosign.push.utils.messages.Response;
import com.kosign.push.utils.enums.PlatformEnum;
import com.kosign.push.utils.enums.ResponseEnum;
import com.kosign.push.notificationHistory.dto.ResponseHistoryDto;



import io.swagger.annotations.Api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@Api(tags = "KOSIGN Push Notification History API")
@PreAuthorize("#oauth2.hasScope('READ')")
// @PreAuthorize("hasRole('ROLE_OPERATOR')")
@RestController
@RequestMapping("/api/v1")
public class NotificationHistoryController extends SuperController{
    @GetMapping("/push/history")
    public Object getHistory(@RequestParam(required = true) String startDate,@RequestParam(required = true) String endDate,String msgTitle) {

        List<ResponseHistoryDto> listHis = historyService.getAllHistory(startDate, endDate, msgTitle);
        
        return Response.getResponseBody(ResponseEnum.Message.SUCCESS,listHis , true);
    }
    
    @GetMapping("/push/history/{id}")
    public Object displayHistory(@PathVariable("id")Integer id){
     ResponseHistoryDto notiHisto= historyService.getPushNotificationHistoryById(id);
     return Response.getResponseBody(ResponseEnum.Message.SUCCESS,notiHisto, true);
    }
}