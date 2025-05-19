package com.test_core.thingsboard.service;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.test_core.thingsboard.common.RawMeterEvent;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class KafkaProducerService {

	  private final KafkaProducer<Long, byte[]> kafkaProducer;
	  
	  public KafkaProducerService(@Value("${queue.kafka.bootstrap.servers}") String bootstrapServers) {
	        this.kafkaProducer = getKafkaProducer(bootstrapServers);
	    }

	  private KafkaProducer<Long, byte[]> getKafkaProducer(String bootstrapServers) {
	        Properties props = new Properties();
	        props.put("bootstrap.servers", bootstrapServers);
	        props.put("key.serializer", LongSerializer.class.getName());
	        props.put("value.serializer", ByteArraySerializer.class.getName());
	        return new KafkaProducer<>(props);
	    }
	  
	 public RawMeterEvent publishMeterRawEvent(Long meterId, Long eventId, Long eventTs, String eventType, Map event, String transitionType) throws IOException {
	        RawMeterEvent rawMeterEvent = null;
	        String topicName = null;
	        if (transitionType.equals("HTTPS"))
	            topicName = "https_events";
	        else if (transitionType.equals("NATS"))
	            topicName = "nats_events";
	        if (topicName != null) {
	            try {
	                rawMeterEvent = setRawMeterEvent(meterId, eventId, eventTs, eventType, event);
	                ProducerRecord<Long, byte[]> record = new ProducerRecord<>(topicName, meterId, toByteArray(rawMeterEvent));
	                RecordMetadata metadata = kafkaProducer.send(record).get();
	                log.info("Message sent to Kafka: Topic - {}, Partition - {}, Offset - {}, MeterId - {}",
	                        metadata.topic(), metadata.partition(), metadata.offset(), meterId);
	            } catch (Exception e) {
	                log.error("Failed to send message to Kafka topic: {}, meterId - {}", topicName, meterId, e);
	                rawMeterEvent = null;
	            }
	        }
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
    
    private byte[] toByteArray(RawMeterEvent rawMeterEvent) throws IOException {
        return new byte[0]; 
    }
}
