package com.test_core.thingsboard.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.test_core.thingsboard.entity.AssetInstallationEntity;

public interface AssetInstallationRepository extends JpaRepository<AssetInstallationEntity, Long> {
    @Query(value = "SELECT hhid FROM barc.asset_installation_data WHERE meter_id = :meterId ORDER BY id DESC LIMIT 1;", nativeQuery = true)
    Long getHouseHoldIdByMeterId(@Param("meterId") Long meterId);
    
  //3
  	@Query("SELECT a.hhid, a.tvId, a.meterInstallationStatusId FROM AssetInstallationEntity a WHERE a.meterId = :meterId ORDER BY a.id DESC")
      List<List> findMeterIdTVIdAndHhid(@Param("meterId") Long meterId);
}
