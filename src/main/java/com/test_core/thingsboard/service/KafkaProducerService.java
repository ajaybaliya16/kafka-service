package com.test_core.thingsboard.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.test_core.thingsboard.common.RawMeterEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaProducerService {

	public RawMeterEvent publishMeterRawEvent(Long meterId, Long eventId, Long eventTs, String eventType, Map event, String transitionType) throws IOException {
	    RawMeterEvent rawMeterEvent;
	    rawMeterEvent = setRawMeterEvent(meterId, eventId, eventTs, eventType, event);
	    return rawMeterEvent;
	}


    private RawMeterEvent setRawMeterEvent(Long meterId, Long eventId, Long eventTs, String eventType, Map event) {
        RawMeterEvent rawMeterEvent = new RawMeterEvent();
        rawMeterEvent.setMeterId(meterId);
        rawMeterEvent.setEventType(eventType);
        LocalDateTime customizedDateTime = (Instant.ofEpochMilli(eventTs).atZone(ZoneId.of("UTC")).toLocalDateTime()).plusHours(3).plusMinutes(30);
        rawMeterEvent.setEventDate(customizedDateTime.toLocalDate());
        rawMeterEvent.setEventId(eventId);
        rawMeterEvent.setEventTs(ZonedDateTime.of(customizedDateTime, ZoneId.of("UTC")).toInstant().toEpochMilli());
        rawMeterEvent.setMeterEventTs(eventTs);
        rawMeterEvent.setCreatedAt(System.currentTimeMillis());
        if (event != null && !event.isEmpty())
            rawMeterEvent.setEvent(event);
        return rawMeterEvent;
    }
}
