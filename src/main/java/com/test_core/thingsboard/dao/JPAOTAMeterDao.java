package com.test_core.thingsboard.dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test_core.thingsboard.Tables.MeterOTAData;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

@Component
@Slf4j
public class JPAOTAMeterDao {

    @PersistenceContext
    private EntityManager entityManager;

    public MeterOTAData getSubmittedAndRunningMeterOTADataByMeterId(String meterId) {
        String sql = "WITH latest_meter_data AS ( " +
                "    SELECT m.*, " +
                "           jsonb_array_elements_text(m.meters) AS meterid, " +
                "           ROW_NUMBER() OVER (PARTITION BY jsonb_array_elements_text(m.meters) ORDER BY m.id DESC) AS rn " +
                "    FROM barc.meter_ota_data m " +
                ") " +
                "SELECT lmd.id, " +
                "       CAST(lmd.meters AS text) AS meters, " +
                "       lmd.ota_summary_id, " +
                "       CAST(lmd.status_id AS text) AS status_id, " +
                "       CAST(lmd.push_success AS text) AS push_success, " +
                "       CAST(lmd.push_errored AS text) AS push_errored, " +
                "       lmd.pushed_by, " +
                "       lmd.pushed_on, " +
                "       CAST(a.hhid AS text) AS hhid, " +
                "       lmd.submitted_on, " +
                "       lmd.cancelled_on, " +
                "       md.name AS status, " +
                "       os.os_branch, " +
                "       os.os_version, " +
                "       os.ota_commit_hash, " +
                "       os.release_note_summary " +
                "FROM latest_meter_data lmd " +
                "JOIN barc.meta_data md ON lmd.status_id = md.meta_data_id " +
                "JOIN barc.ota_summary os ON lmd.ota_summary_id = os.id " +
                "LEFT JOIN barc.asset_installation_data a ON a.meter_id = CAST(lmd.meterid AS bigint) " +
                "WHERE lmd.rn = 1 " +
                "AND lmd.meterid = :meterId " +
                "AND lmd.status_id in (26,25)  " +
                "LIMIT 1;";

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("meterId", meterId);
        try {
            Object[] row = (Object[]) query.getSingleResult();
            if (row != null) {
                return new MeterOTAData(
                        Long.valueOf(row[0].toString()),
                        new ObjectMapper().readValue((String) row[1], new TypeReference<List<Long>>() {
                        }),
                        Long.valueOf(row[2].toString()),
                        row[3] != null ? Long.valueOf(row[3].toString()) : null,
                        row[4] != null ? new ObjectMapper().readValue((String) row[4], new TypeReference<List<Long>>() {
                        }) : null,
                        row[5] != null ? new ObjectMapper().readValue((String) row[5], new TypeReference<List<Long>>() {
                        }) : null,
                        row[6] != null ? (String) row[6] : null,
                        row[7] != null ? (Timestamp) row[7] : null,
                        row[8] != null ? Long.valueOf(row[8].toString()) : null,
                        row[9] != null ? (Timestamp) row[9] : null,
                        row[10] != null ? (Timestamp) row[10] : null,
                        row[11] != null ? (String) row[11] : null,
                        row[12] != null ? (String) row[12] : null,
                        row[13] != null ? (String) row[13] : null,
                        row[14] != null ? (String) row[14] : null,
                        row[15] != null ? (String) row[15] : null,
                        null,
                        null,
                        null
                );
            }
        } catch (Exception e) {
            System.out.println("Error while fetching getLatestMeterOTAData " + e);
        }
        return null;
    }
}