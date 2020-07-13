package com.kosign.push.notificationHistory;

import java.util.List;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.notificationHistory.dto.ResponseHistoryDto;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@Api(tags = "Notifications History")
@PreAuthorize("#oauth2.hasScope('READ')")
// @PreAuthorize("hasRole('ROLE_OPERATOR')")
@RestController
@RequestMapping("/api/v1")
public class NotificationHistoryController
{
	@Autowired
    public NotificationHistoryService historyService;
	
    @GetMapping("/push/history")
    public Object findAllNotificationHistories
    (
		@RequestParam(value="startDate", required=true) String startDate,
		@RequestParam(value="endDate"  , required=true) String endDate  ,
		@RequestParam(value="title"    , required=false) String title    
    ) 
    {
        List<ResponseHistoryDto> respData = historyService.getAllHistory(startDate, endDate, title);
        
        return Response.setResponseEntity(HttpStatus.OK, respData);
    }
    
    @GetMapping("/push/history/{id}")
    public Object findNotificationHistoryById(@PathVariable("id")Integer id)
    {
    	ResponseHistoryDto respData= historyService.getPushNotificationHistoryById(id);
    	return Response.setResponseEntity(HttpStatus.OK, respData);
    }
}