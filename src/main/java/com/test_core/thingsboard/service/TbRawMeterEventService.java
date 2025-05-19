package com.test_core.thingsboard.service;


import java.io.IOException;
import java.util.List;
import java.util.Map;
public interface TbRawMeterEventService {

    List<Map<String, Long>> saveListOfChannelLogs(Long meterId, String batteryPercentage, String networkPercentage, byte[] channelLogs) throws IOException;

}
