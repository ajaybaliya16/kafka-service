package com.test_core.thingsboard.entity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.test_core.thingsboard.Tables.HouseHold;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import static com.test_core.thingsboard.entity.ModelConstants.*;

@Data
//@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Table(schema = "BARC", name = "HOUSEHOLD")
public class HouseHoldEntity {


//    public class HouseHoldEntity {
//	package org.thingsboard.server.dao.model.sql;
//
//(schema = BARC_SCHEMA, name = HOUSE_HOLD_TABLE_NAME)
//	public class HouseHoldEntity implements ToData<HouseHold> {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;
        @Column(name = HHID)
        private Long hhid;
        //	@Type(type = "jsonb")
        @Column(name = TV_IDS, columnDefinition = "jsonb")
        private List<String> tvIds;
        @Column(name = W_ID)
        private Long wId;
        //	@Type(type = "jsonb")
        @Column(name = MEMBER_NAMES, columnDefinition = "jsonb")
        private List<String> memberNames;
        @Column(name = STATUS_ID)
        private Long statusId;
        @Column(name = PROFILE_ID)
        private Long profileId;
        @Column(name = TV_COUNT)
        private Integer tvCount;
        @Column(name = MEMBER_COUNT)
        private Integer memberCount;
        @Column(name = VALID_FROM_TIME_PROPERTY)
        private Timestamp validFrom;
        @Column(name = VALID_TO_TIME_PROPERTY)
        private Timestamp validTo;
        //	@Type(type = "jsonb")
        @Column(name = IS_ACTIVE_MEMBER, columnDefinition = "jsonb")
        private List<String> isActiveMember;
        //	@Type(type = "jsonb")
        @Column(name = IS_ACTIVE_TV, columnDefinition = "jsonb")
        private List<String> isActiveTV;
        private String updatedBy;
        @Column(name = CREATED_TIME_PROPERTY)
        private Timestamp createdTime;
        @Column(name = UPDATED_TIME_PROPERTY)
        private Timestamp updatedTime;
        @Transient
        private String profileName;
        @Column(name = STATUS_ID_VALID_FROM_TIME_PROPERTY)
        private Timestamp statusIdValidFrom;
        @Column(name = STATUS_ID_VALID_TO_TIME_PROPERTY)
        private Timestamp statusIdValidTo;
        @Column(name = PROFILE_ID_VALID_FROM_TIME_PROPERTY)
        private Timestamp profileIdValidFrom;
        @Column(name = PROFILE_ID_VALID_TO_TIME_PROPERTY)
        private Timestamp profileIdValidTo;
        @Column(name = MEMBER_ID_VALID_FROM_TIME_PROPERTY)
        private Timestamp memberIdValidFrom;
        @Column(name = MEMBER_ID_VALID_TO_TIME_PROPERTY)
        private Timestamp memberIdValidTo;

        public HouseHoldEntity() {
            super();
        }

        public HouseHoldEntity(HouseHold houseHold) {
            if (houseHold.getId() != null)
                this.id = houseHold.getId();
            this.validFrom = houseHold.getValidFrom();
            this.validTo = houseHold.getValidTo();
            this.hhid = houseHold.getHhid();
            this.wId = houseHold.getWId();
            this.tvIds = houseHold.getTv_ids();
            this.memberNames = houseHold.getMember_names();
            this.statusId = houseHold.getStatusId();
            this.profileId = houseHold.getProfileId();
            this.tvCount = houseHold.getTv_count();
            this.memberCount = houseHold.getMember_count();
            this.updatedBy = houseHold.getUpdatedBy();
            this.isActiveMember = houseHold.getActive_members();
            this.isActiveTV = houseHold.getActive_tvs();
            this.createdTime = houseHold.getCreatedTime();
            this.updatedTime = houseHold.getUpdatedTime();
            this.statusIdValidFrom = houseHold.getStatusIdValidFrom();
            this.statusIdValidTo = houseHold.getStatusIdValidTo();
            this.profileIdValidFrom = houseHold.getProfileIdValidFrom();
            this.profileIdValidTo = houseHold.getProfileIdValidTo();
            this.memberIdValidFrom = houseHold.getMemberIdValidFrom();
            this.memberIdValidTo = houseHold.getMemberIdValidTo();

        }

        public HouseHoldEntity(HouseHoldEntity houseHoldEntity) {
            this.id = houseHoldEntity.getId();
            this.validFrom = houseHoldEntity.getValidFrom();
            this.validTo = houseHoldEntity.getValidTo();
            this.hhid = houseHoldEntity.getHhid();
            this.wId = houseHoldEntity.getWId();
            this.tvIds = houseHoldEntity.getTvIds();
            this.memberNames = houseHoldEntity.getMemberNames();
            this.statusId = houseHoldEntity.getStatusId();
            this.profileId = houseHoldEntity.getProfileId();
            this.tvCount = houseHoldEntity.getTvCount();
            this.memberCount = houseHoldEntity.getMemberCount();
            this.updatedBy = houseHoldEntity.getUpdatedBy();
            this.isActiveMember = houseHoldEntity.getIsActiveMember();
            this.isActiveTV = houseHoldEntity.getIsActiveTV();
            this.createdTime = houseHoldEntity.getCreatedTime();
            this.updatedTime = houseHoldEntity.getUpdatedTime();
            this.statusIdValidFrom = houseHoldEntity.getStatusIdValidFrom();
            this.statusIdValidTo = houseHoldEntity.getStatusIdValidTo();
            this.profileIdValidFrom = houseHoldEntity.getProfileIdValidFrom();
            this.profileIdValidTo = houseHoldEntity.getProfileIdValidTo();
            this.memberIdValidFrom = houseHoldEntity.getMemberIdValidFrom();
            this.memberIdValidTo = houseHoldEntity.getMemberIdValidTo();
        }

        public HouseHoldEntity(HouseHoldEntity houseHoldEntity, String profileName) {
            this(houseHoldEntity);
            this.profileName = profileName;
        }


        public HouseHold toData() {
            HouseHold houseHold = new HouseHold();
            houseHold.setId(this.id);
            houseHold.setHhid(this.hhid);
            houseHold.setWId(this.wId);
            houseHold.setTv_ids(this.tvIds);
            houseHold.setMember_names(this.memberNames);
            houseHold.setTv_count(this.tvCount);
            houseHold.setMember_count(this.memberCount);
            houseHold.setStatusId(this.statusId);
            houseHold.setProfileId(this.profileId);
            houseHold.setUpdatedBy(this.updatedBy);
            houseHold.setValidFrom(this.validFrom);
            houseHold.setValidTo(this.validTo);
            houseHold.setActive_tvs(this.isActiveTV);
            houseHold.setActive_members(this.isActiveMember);
            houseHold.setCreatedTime(this.createdTime);
            houseHold.setUpdatedTime(this.updatedTime);
            if (this.statusId != null)
                houseHold.setStatusName(getMetaDataName(this.statusId));
            if (this.profileId != null)
                houseHold.setProfileName(this.profileName);
            houseHold.setStatusIdValidFrom(this.statusIdValidFrom);
            houseHold.setStatusIdValidTo(this.statusIdValidTo);
            houseHold.setProfileIdValidFrom(this.profileIdValidFrom);
            houseHold.setProfileIdValidTo(this.profileIdValidTo);
            houseHold.setMemberIdValidFrom(this.memberIdValidFrom);
            houseHold.setMemberIdValidTo(this.memberIdValidTo);
            return houseHold;
        }
        private static Map<Long, String> metaDataIdMap = new ConcurrentHashMap<>();

        public static String getMetaDataName(Long metaDataId) {
            String name = null;
            if (metaDataIdMap.containsKey(metaDataId))
                name = metaDataIdMap.get(metaDataId);
            return name;
        }
    }

