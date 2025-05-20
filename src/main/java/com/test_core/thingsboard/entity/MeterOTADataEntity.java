package com.test_core.thingsboard.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.test_core.thingsboard.Tables.MeterOTAData;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Entity
@Table
public class MeterOTADataEntity {

    public static final String ID_PROPERTY = "id";
    private static final String OTA_SUMMARY_ID = "OTA_SUMMARY_ID";
    private static final String CANCELLED_ON = "CANCELLED_ON";
    private static final String SUBMITTED_ON = "SUBMITTED_ON";
    private static final String PUSHED_ON = "PUSHED_ON";
    private static final String PUSHED_BY = "PUSHED_BY";
    private static final String PUSH_ERRORED = "PUSH_ERRORED";
    private static final String PUSH_SUCCESS = "PUSH_SUCCESS";
    private static final String STATUS_ID = "STATUS_ID";
    private static final String METERS = "METERS";
    //	MeterOTADatapackage org.thingsboard.server.dao.model.sql;
//
//import static org.thingsboard.server.dao.model.ModelConstants.*;
//import static org.thingsboard.server.dao.util.DataUtils.getMetaDataName;
//
//import java.sql.Timestamp;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.persistence.*;
//
//import org.hibernate.annotations.Type;
//import org.thingsboard.server.dao.model.ToData;
//
//import ch.qos.logback.core.model.ModelConstants;
//import jakarta.persistence.Column;
//import jakarta.persistence.Entity;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.Table;
//import lombok.Data;
//
//(schema = BARC_SCHEMA, name = METER_OTA_DATA_TABLE_NAME)
//public class MeterOTADataEntity implements ToData<MeterOTAData> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ModelConstants.ID_PROPERTY)
    private Long id;
      @Type(type = "jsonb")
    @Column(name = METERS, columnDefinition = "jsonb")
    private List<Long> meters;
    @Column(name = STATUS_ID)
    private Long statusId;
    @Type(type = "jsonb")
    @Column(name = PUSH_SUCCESS, columnDefinition = "jsonb")
    private List<Long> pushSuccess;
    @Type(type = "jsonb")
    @Column(name = PUSH_ERRORED, columnDefinition = "jsonb")
    private List<Long> pushErrored;
    @Column(name = PUSHED_BY)
    private String pushedBy;
    @Column(name = PUSHED_ON)
    private Timestamp pushedOn;
    @Column(name = SUBMITTED_ON)
    private Timestamp submittedOn;
    @Column(name = CANCELLED_ON)
    private Timestamp cancelledOn;
    @Column(name = OTA_SUMMARY_ID)
    private Long otaSummaryId;

    public MeterOTADataEntity() {
        super();
    }

    public MeterOTADataEntity(MeterOTAData meterOTAData) {
        if (meterOTAData.getId() == null)
            this.id = meterOTAData.getId();
        this.cancelledOn = meterOTAData.getCancelledOn();
        this.meters = meterOTAData.getMeters();
        this.pushErrored = meterOTAData.getPushErrored();
        this.otaSummaryId = meterOTAData.getOtaSummaryId();
        this.pushedBy = meterOTAData.getPushedBy();
        this.pushedOn = meterOTAData.getPushedOn();
        this.pushSuccess = meterOTAData.getPushSuccess();
        this.statusId = meterOTAData.getStatusId();
        this.submittedOn = meterOTAData.getSubmittedOn();

    }

    public MeterOTAData toData() {
        MeterOTAData meterOTAData = new MeterOTAData();
        meterOTAData.setId(id);
        meterOTAData.setOtaSummaryId(otaSummaryId);
        meterOTAData.setCancelledOn(cancelledOn);
        meterOTAData.setPushSuccess(pushSuccess);
        meterOTAData.setPushErrored(pushErrored);
        meterOTAData.setStatusId(statusId);
        meterOTAData.setMeters(meters);
        meterOTAData.setPushedBy(pushedBy);
        meterOTAData.setPushedOn(pushedOn);
        meterOTAData.setSubmittedOn(submittedOn);
        if (statusId != null)
            meterOTAData.setStatus(getMetaDataName(statusId));
        return meterOTAData;
    }

    private String getMetaDataName(Long statusId2) {
        // TODO Auto-generated method stub
        return null;
    }

    private List<Long> transformList(List<?> mixedList) {
        return mixedList == null ? new ArrayList<>()
                : mixedList.stream()
                .map(element -> {
                    if (element instanceof Integer) {
                        return ((Integer) element).longValue();
                    } else if (element instanceof Long) {
                        return (Long) element;
                    } else {
                        throw new IllegalArgumentException(
                                "Unsupported type in list: " + element.getClass().getName());
                    }
                })
                .collect(Collectors.toList());
    }
}
