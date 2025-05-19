package com.test_core.thingsboard.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test_core.thingsboard.service.TbRawMeterEventService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RawMeterEventController {


    private final TbRawMeterEventService tbRawMeterEventService;

    @PostMapping( value = "/savelistofchannellogs/compress", consumes = {"application/octet-stream", "application/gzip"})
    	public ResponseEntity<?> saveListOfChannelLogs(
    	    @RequestBody(required = false) byte[] compressedChannelLogs,
    	    @RequestParam("meterId") Long meterId,
    	    @RequestParam("batteryPercentage") String batteryPercentage,
    	    @RequestParam("networkPercentage") String networkPercentage
    	) throws IOException {

    	    if (compressedChannelLogs == null || compressedChannelLogs.length == 0) {
    	        Map<String, Object> response = new HashMap<>();
    	        response.put("id", meterId);
    	        response.put("message", "Request body is missing or empty");
    	        return ResponseEntity.badRequest().body(response);
    	    }

    	    List<Map<String, Long>> channelLogList = tbRawMeterEventService.saveListOfChannelLogs(
    	            meterId, batteryPercentage, networkPercentage, compressedChannelLogs
    	    );

    	    if (!channelLogList.isEmpty()) {
    	        return ResponseEntity.ok(channelLogList);
    	    } else {
    	        Map<String, Object> response = new HashMap<>();
    	        response.put("id", meterId);
    	        response.put("message", "ChannelLogs Data not saved");
    	        return ResponseEntity.badRequest().body(response);
    	    }
    	}




}
