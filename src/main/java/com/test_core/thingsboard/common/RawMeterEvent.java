package com.test_core.thingsboard.common;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class RawMeterEvent implements Serializable {

	 private Long meterId;
	    private String eventType;
	    private LocalDate eventDate;
	    private Long eventId;
	    private Long eventTs;
	    private Long createdAt;
	    private Long embeddedTimestamp;
	    private Long beginningTimestamp;
	    private Long timestampEventTimestamp;
	    private String timeState;
	    private Long timeStateOffset;
	    private Long duration;
	    private Map event;
	    private Long meterEventTs;
}
