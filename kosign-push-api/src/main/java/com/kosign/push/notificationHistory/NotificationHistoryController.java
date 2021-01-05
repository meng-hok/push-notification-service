package com.kosign.push.notificationHistory;

import java.time.*;
import java.time.format.*;
import java.util.*;

import com.kosign.push.configs.annatations.DateFormat;
import com.kosign.push.utils.messages.Response;
import com.kosign.push.notificationHistory.dto.RequestHistoryList;
import com.kosign.push.notificationHistory.dto.ResponseHistoryList;
import io.swagger.annotations.Api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Validated
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
            @RequestParam(value="app_id"    )  @Valid String appId,
            @RequestParam(value="start_date" )@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate ,
            @RequestParam(value="end_date" )@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate  ,
            @RequestParam(value="title"     , required=false) String title ,
            @RequestParam(value = "receiver_id",required = false) String receiverId,
            @RequestParam(value="page_num"     , defaultValue = "1") int pageNum,
            @RequestParam(value="page_size",defaultValue = "10") int pageSize
    ) 
    {
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formmatDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String convertedStartDate = startDate.toInstant().toString().split("T")[0];
//        String convertedEndDate = endDate.toInstant().toString().split("T")[0];

        String convertedStartDate =startDate.atStartOfDay(zoneId).format(formmatDate);
        String convertedEndDate = endDate.atStartOfDay(zoneId).format(formmatDate);
    	RequestHistoryList request = new RequestHistoryList();
    	request.setAppId(appId);
    	request.setStartDate(convertedStartDate);
    	request.setEndDate(convertedEndDate);
    	request.setMsgTitle(title);
    	request.setReceiverId(receiverId);

        HashMap<String,Object> response = historyService.findAllHistories(request,pageNum,pageSize);

        return Response.setResponseEntity(HttpStatus.OK, response);
    }
    
    @GetMapping("/push/history/{id}")
    public Object findNotificationHistoryById(@PathVariable("id")Integer id)
    {
    	ResponseHistoryList response = historyService.findNotificationHistoryById(id);
    	return Response.setResponseEntity(HttpStatus.OK, response);
    }

    @GetMapping("/push/history/bulk/{bulk_id}")
    public Object findNotificationHistoryByBulkId(@PathVariable("bulk_id") @NotNull String id)
    {
        List<ResponseHistoryList> response = historyService.findNotificationHistoryByBulkId(id);
        return Response.setResponseEntity(HttpStatus.OK, response);
    }
}