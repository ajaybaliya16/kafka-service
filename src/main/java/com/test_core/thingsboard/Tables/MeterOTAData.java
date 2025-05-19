package com.test_core.thingsboard.Tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeterOTAData {
    private Long id;
    private List<Long> meters;
    private Long otaSummaryId;
    private Long statusId;
    private List<Long> pushSuccess;
    private List<Long> pushErrored;
    private String pushedBy;
    private Timestamp pushedOn;
    private Long hhid;
    private Timestamp submittedOn;
    private Timestamp cancelledOn;
    private String status;
    private String osBranch;
    private String osVersion;
    private String otaCommitHash;
    private String releaseNoteSummary;
    private Long osTypeID;
    private String osType;
    private String hardwareVersion;
}
