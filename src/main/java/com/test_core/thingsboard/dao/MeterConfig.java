package com.test_core.thingsboard.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterConfig {

//	package org.thingsboard.server.common.data.meterconfig;

    //	public class MeterConfig {
    private Long id;
    private Long meterId;
    private Long technicalConfigId;
    private String appliedValue;
    private String serverValue;
    private boolean isReSync;
    private Timestamp submissionAt;
    private Timestamp appliedAt;
    private String submittedBy;
    private Long statusId;
    private String configType;
    private String status;
    private Long hhid;

    public MeterConfig(Long id, Long meterId, Long technicalConfigId, String appliedValue, String serverValue,
                       Boolean isReSync, Timestamp submissionAt, Timestamp appliedAt, String submittedBy, Long statusId, Long hhid,
                       String status, String configType) {
        if (id != null)
            this.id = id;
        this.meterId = meterId;
        this.technicalConfigId = technicalConfigId;
        this.appliedValue = appliedValue;
        this.serverValue = serverValue;
        this.statusId = statusId;
        this.isReSync = isReSync;
        this.submissionAt = submissionAt;
        this.appliedAt = appliedAt;
        this.submittedBy = submittedBy;
        this.hhid = hhid;
        this.status = status;
        this.configType = configType;
    }

}
