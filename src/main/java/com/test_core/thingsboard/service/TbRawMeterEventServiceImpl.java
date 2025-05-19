package com.test_core.thingsboard.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_core.thingsboard.common.RawMeterEvent;

import lombok.RequiredArgsConstructor;
import com.test_core.thingsboard.common.*;

@Service
@RequiredArgsConstructor
public class TbRawMeterEventServiceImpl implements TbRawMeterEventService {

    private final KafkaProducerService kafkaProducerService;
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<Map<String, Long>> saveListOfChannelLogs(Long meterId, String batteryPercentage, String networkPercentage, byte[] channelLogs) throws IOException {
        List<Map<String, Long>> channelLogList = new ArrayList<>();

        // Previously used decompression, but now we assume the data is plain JSON bytes.
        // List<channelLog> channelLogsData = decompressGzip(channelLogs);

        // Directly deserialize the JSON from the byte array, assuming plain JSON data.
        List<channelLog> channelLogsData = new ObjectMapper().readValue(channelLogs, new TypeReference<List<channelLog>>() {});

        for (channelLog channelLog : channelLogsData) {
            String eventType = switch (channelLog.getEventType()) {
                case "0", "1", "2" -> "watermark_channel";
                default -> channelLog.getEventType();
            };

            Long eventTs;
            try {
                eventTs = LocalDateTime.parse(channelLog.getDeviceTimestamp(), DATETIME_FORMATTER)
                        .atZone(ZoneId.of("Asia/Kolkata")).toInstant().toEpochMilli();
            } catch (Exception e) {
                eventTs = Instant.now().toEpochMilli();
            }

            Map<String, Object> event = setEventData(channelLog, batteryPercentage, networkPercentage, eventType);
            RawMeterEvent rawMeterEvent = kafkaProducerService.publishMeterRawEvent(meterId, channelLog.getEventId(), eventTs, eventType, event, "HTTPS");

            if (rawMeterEvent != null) {
                Map<String, Long> map = new HashMap<>();
                map.put("eventId", channelLog.getEventId());
                map.put("id", meterId);
                channelLogList.add(map);
            }
        }

        return channelLogList;
    }


    private List<channelLog> decompressGzip(byte[] channelLogs) {
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(channelLogs))) {
        	return new ObjectMapper().readValue(new String(gzipInputStream.readAllBytes()), new TypeReference<List<channelLog>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<String, Object> setEventData(channelLog channelLog, String batteryPercentage, String networkPercentage, String eventType) {
        Map<String, Object> event = new HashMap<>();
        if (eventType != null && !eventType.isEmpty()) {
            if (eventType.equalsIgnoreCase(getEventTypeById(2)))
                event = setGuestRegistration(channelLog.getFriend(), event);
            if (eventType.equalsIgnoreCase(REMOTE_ACTIVITY_EVENT_TYPE))
                event = setRemoteActivity(channelLog.getEventAction(), event);
            if (eventType.equalsIgnoreCase(VIEWER_DECLARATION_EVENT_TYPE))
                event = setViewerDeclaration(channelLog.getMemberDeclaration(), channelLog.getFriend(), channelLog.getConfidence(), event);
            if (eventType.equalsIgnoreCase(INSTALL_READY_EVENT_TYPE))
                event = setInstallReady(channelLog.getHhid(), channelLog.getRemoteId(), event);
            if (eventType.equalsIgnoreCase("0") || eventType.equalsIgnoreCase("1") || eventType.equalsIgnoreCase("2"))
                event = setWatermarkChannel(channelLog.getReferenceTime(), eventType, channelLog.getWmkIdString(), channelLog.getConfidence(), channelLog.getOffset(), event);
            if (eventType.equalsIgnoreCase(WATERMARK_TIMESTAMP_EVENT_TYPE))
                event = setWatermarkTimestamp(channelLog.getReferenceTime(), channelLog.getWaterMarkTimestamp(), channelLog.getConfidence(), channelLog.getOffset(), event);
            if (eventType.equalsIgnoreCase(METER_INSTALLATION_EVENT_TYPE))
                event = setMeterInstallation(channelLog.getHhid(), channelLog.getRemoteId(), channelLog.isSuccess(), event);
            if (eventType.equalsIgnoreCase(NETWORK_LATCH_EVENT_TYPE))
                event = setNetworkLatch(channelLog.getEventAction(), event);
            if (eventType.equalsIgnoreCase(DERIVED_TV_STATS_EVENT_TYPE) || eventType.equalsIgnoreCase(BOOT_EVENT_TYPE))
                event = setDerivedTvStatusAndBoot(channelLog.getEventAction(), event);
            if (eventType.equalsIgnoreCase(getEventTypeById(109)))
                event = setDayCutOff(channelLog, event);
        }

        if (batteryPercentage != null && !batteryPercentage.isEmpty())
            event.put("batteryPercentage", batteryPercentage);
        if (batteryPercentage != null && !batteryPercentage.isEmpty())
            event.put("networkPercentage", networkPercentage);

        return event;
    }

    // Stub methods below — replace with actual implementations
    private String getEventTypeById(int id) { return "some_event_type"; }

    private Map<String, Object> setGuestRegistration(String friend, Map<String, Object> event) { return event; }
    private Map<String, Object> setRemoteActivity(String action, Map<String, Object> event) { return event; }
    private Map<String, Object> setViewerDeclaration(String declaration, String friend, Integer confidence, Map<String, Object> event) { return event; }
    private Map<String, Object> setInstallReady(Long hhid, Long remoteId, Map<String, Object> event) { return event; }
    private Map<String, Object> setWatermarkChannel(String refTime, String eventType, String wmkIdString, Integer confidence, Long offset, Map<String, Object> event) { return event; }
    private Map<String, Object> setWatermarkTimestamp(String refTime, String wmtTs, Integer confidence, Long offset, Map<String, Object> event) { return event; }
    private Map<String, Object> setMeterInstallation(Long hhid, Long remoteId, boolean success, Map<String, Object> event) { return event; }
    private Map<String, Object> setNetworkLatch(String action, Map<String, Object> event) { return event; }
    private Map<String, Object> setDerivedTvStatusAndBoot(String action, Map<String, Object> event) { return event; }
    private Map<String, Object> setDayCutOff(channelLog log, Map<String, Object> event) { return event; }

    // Constants for event type names
    private static final String REMOTE_ACTIVITY_EVENT_TYPE = "REMOTE_ACTIVITY";
    private static final String VIEWER_DECLARATION_EVENT_TYPE = "VIEWER_DECLARATION";
    private static final String INSTALL_READY_EVENT_TYPE = "INSTALL_READY";
    private static final String WATERMARK_TIMESTAMP_EVENT_TYPE = "WATERMARK_TIMESTAMP";
    private static final String METER_INSTALLATION_EVENT_TYPE = "METER_INSTALLATION";
    private static final String NETWORK_LATCH_EVENT_TYPE = "NETWORK_LATCH";
    private static final String DERIVED_TV_STATS_EVENT_TYPE = "DERIVED_TV_STATS";
    private static final String BOOT_EVENT_TYPE = "BOOT";
}
