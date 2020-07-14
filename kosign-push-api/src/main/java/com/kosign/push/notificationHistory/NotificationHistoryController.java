package com.kosign.push.notificationHistory;

import java.util.List;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.notificationHistory.dto.RequestHistoryList;
import com.kosign.push.notificationHistory.dto.ResponseHistoryList;
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
      @RequestParam(value="app_id"    ) String appId,
      @RequestParam(value="start_date") String startDate,
      @RequestParam(value="end_date"  ) String endDate  ,
      @RequestParam(value="title"     , required=false) String title    
    ) 
    {
    	RequestHistoryList request = new RequestHistoryList();
    	request.setAppId(appId);
    	request.setStartDate(startDate);
    	request.setEndDate(endDate);
    	request.setMsgTitle(title);
    	
        List<ResponseHistoryList> response = historyService.findAllHistories(request);
        
        return Response.setResponseEntity(HttpStatus.OK, response);
    }
    
    @GetMapping("/push/history/{id}")
    public Object findNotificationHistoryById(@PathVariable("id")Integer id)
    {
    	ResponseHistoryList response = historyService.findNotificationHistoryById(id);
    	return Response.setResponseEntity(HttpStatus.OK, response);
    }
}