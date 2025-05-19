package com.test_core.thingsboard.service;

import com.test_core.thingsboard.Tables.MeterOTAData;
import com.test_core.thingsboard.dao.JPAOTAMeterDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class DefaultMeterOTADataService implements TbMeterOTADataService{
    @Autowired
    private JPAOTAMeterDao jpaotaMeterDao;

    public Map<String, Object> getlatestbuildfilestatus(Long meterId) {
        Map<String, Object> response = new HashMap<>();
        MeterOTAData meterOTAData = jpaotaMeterDao.getSubmittedAndRunningMeterOTADataByMeterId(String.valueOf(meterId));
        if (meterOTAData != null) {
            response.put("id", meterOTAData.getId());
            response.put("updateDate", meterOTAData.getPushedOn() != null ? meterOTAData.getPushedOn().toString() : meterOTAData.getPushedOn());
            response.put("createDate", meterOTAData.getSubmittedOn() != null ? meterOTAData.getSubmittedOn().toString() : meterOTAData.getSubmittedOn());
            response.put("build_version", meterOTAData.getOsVersion());
//            response.put("filename", getFileNameWithUrl(meterOTAData.getOtaCommitHash()));
            response.put("build_release_note", meterOTAData.getReleaseNoteSummary());
            response.put("download_link", meterOTAData.getOtaCommitHash());
            response.put("meterIdsList", meterOTAData.getMeters() != null && !meterOTAData.getMeters().isEmpty() ? meterOTAData.getMeters() : meterOTAData.getMeters());
            response.put("buildForAll", false);
        }
        return response;
    }
}
