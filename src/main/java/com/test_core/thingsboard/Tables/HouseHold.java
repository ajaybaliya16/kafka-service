package com.test_core.thingsboard.Tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HouseHold {
    //@ApiModel
//public class HouseHold {
    private Long id;
    private Long hhid;
    private Long wId;
    private List<String> tv_ids;
    private List<String> member_names;
    private Long statusId;
    private Long profileId;
    private Timestamp validFrom;
    private Timestamp validTo;
    private Integer tv_count;
    private Integer member_count;
    private List<String> active_tvs;
    private List<String> active_members;
    private String updatedBy;
    private String profileName;
    private String statusName;
    private Timestamp createdTime;
    private Timestamp updatedTime;
    private Timestamp statusIdValidFrom;
    private Timestamp statusIdValidTo;
    private Timestamp profileIdValidFrom;
    private Timestamp profileIdValidTo;
    private Timestamp memberIdValidFrom;
    private Timestamp memberIdValidTo;
}
