package com.test_core.thingsboard.common;

import lombok.Data;

@Data
public class channelLog {

	  private Integer confidence;
	    private String deviceTimestamp;
	    private boolean encrypted;
	    private String eventAction;
	    private Long eventId;
	    private String eventType;
	    private String friend;
	    private String memberDeclaration;
	    private Long offset;
	    private String referenceTime;
	    private String waterMarkTimestamp;
	    private String wmkId;
	    private String wmkIdString;
	    private Long hhid;
	    private Long remoteId;
	    private boolean success;
}
