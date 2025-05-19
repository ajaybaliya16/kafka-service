package com.test_core.thingsboard.service;


import static com.test_core.thingsboard.dao.DataUtils.getMetaDataName;
import static com.test_core.thingsboard.entity.ModelConstants.BM2;
import static com.test_core.thingsboard.entity.ModelConstants.BM3;
import static com.test_core.thingsboard.entity.ModelConstants.BM4;
import static com.test_core.thingsboard.entity.ModelConstants.BMA_1_0_VERSION;
import static com.test_core.thingsboard.entity.ModelConstants.BMA_SET_VERSION;
import static com.test_core.thingsboard.dao.TimeUtils.getProductionAndValidFromDate;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test_core.thingsboard.common.KantarLicense;
import com.test_core.thingsboard.common.SimData;
import com.test_core.thingsboard.dao.JpaAssetInstallationDao;
import com.test_core.thingsboard.dao.MeterDao;
import com.test_core.thingsboard.entity.Meter;
@Service
public class DefaultTbMeterServiceImp implements TbMeterService {
	
	@Autowired
    MeterService meterService;
	
	@Autowired
	JpaAssetInstallationDao jpaAssetInstallationDao;
	
	@Autowired
	MeterDao meterDao;
	
	@Autowired
    TbSimDataService tbSimDataService;
	
	@Autowired
	KantarLicensingService licensingService;
	
	 @Override
	    public void signupdevice(String deviceID, Double lat, Double lng, String meterId, String
	            deviceSerialNumber, String assetSerialNumber, String simSerialNumber, String imeiNumber1,
	                             String imeiNumber2, String simOperator, String networkOperator, String networkOperatorName,
	                             String osVersion, String appVersion, String ethMac, String motherBoardSerialNo, String imsi, String simOperatorName) throws
	            Exception {
	        Meter meter = null;
	        try {
	            meter = meterService.fetchByDeviceId(deviceID);
	            if (meter == null)
	                meter = new Meter();
	            SimData simData = tbSimDataService.saveSimData(deviceID, meterId, imsi, simSerialNumber, simOperatorName, null, null);
	            if (simData == null)
	                throw new Exception("Sim data not saved");
	            meter.setSim1ImsiId(simData.getId());
	            meter.setDeviceId(deviceID);
	            meter.setImeiNumber1(imeiNumber1);
	            meter.setImeiNumber2(imeiNumber2);
	            meter.setSimOperator(simOperator);
	            meter.setNetworkOperator(networkOperator);
	            meter.setNetworkOperatorName(networkOperatorName);
	            if (lat != null)
	                meter.setLatitude(lat.toString());
	            if (lng != null)
	                meter.setLongitude(lng.toString());
	            if (meterId != null && !deviceID.equals(meterId))
	                meter.setMeterId(Long.valueOf(meterId));
	            meter.setSwVersion(appVersion);
	            meter.setDeviceSerialNumber(deviceSerialNumber);
	            meter.setAssetSerialNumber(assetSerialNumber);
	            meter.setCreatedTime(new Timestamp(System.currentTimeMillis()));
	            meterDao.saveMeter(meter);
	        } catch (Exception e) {
	            throw new Exception(e.getMessage());//, ThingsboardErrorCode.GENERAL);
	        }
	    }
	 
	 @Override
	    public String getdevicelicensekey(String deviceID) {
	        return meterDao.getdevicelicensekey(deviceID);
	    }

	 @Override
	    public String generatedevicelicensekey(String deviceID) throws Exception {
	        String licenseKey = null;
	        Meter meter = meterService.fetchByDeviceId(deviceID);
	        if (meter != null) {
	            if (meter.getLicenseKey() == null) {
	            	// THIRD PARTY API TO GET LICENSE 
	                KantarLicense kantarLicense = licensingService.createLicense(38L, deviceID);
	                if (kantarLicense != null) {
	                	  meter.setLicenseNo(kantarLicense.getCivolutionSerialNumber());
	                      meter.setLicenseKey(kantarLicense.getCivolutionLicense());
	                      meterDao.saveMeter(meter);
	                      licenseKey = kantarLicense.getCivolutionLicense();
	                }
	            } else
	                licenseKey = meter.getLicenseKey();
	        }
	        return licenseKey;
	    }
	 
	 @Override
	    public String saveapplatestversion(String deviceID, String appVersion, String appUpdatedDate) throws
	            Exception {
	        String version = null;
	        Meter meter = null;
	        try {
	            meter = meterService.fetchByDeviceId(deviceID);
	            if (meter != null) {
	                meter.setSwVersion(appVersion);
	                if (appUpdatedDate != null) {
	                    LocalDateTime localDateTime;
	                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	                    try {
	                        localDateTime = LocalDateTime.parse(appUpdatedDate, formatter);
	                    } catch (Exception e) {
	                        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	                        localDateTime = LocalDateTime.parse(appUpdatedDate, formatter);
	                    }
	                    Instant instant = localDateTime.atZone(ZoneId.of("UTC")).toInstant();
	                    meter.setUpdatedTime(new Timestamp(instant.toEpochMilli()));
	                }
	                meter = meterDao.saveMeter(meter);
	                version = meter.getSwVersion();
	            }
	        } catch (Exception e) {
	            throw new Exception(e.getMessage());
	        }
	        return version;
	    }
	    @Override
	    public Map<String, Object> getmeterid(String deviceID, String motherBoardSerialNo, String ethMac) throws
	            Exception {
	        Map<String, Object> response = new HashMap<>();
	        Meter meter = null;
	        meter = meterService.fetchByDeviceId(deviceID);
	        if (meter != null) {
	            meter.setMotherboardSerialNo(motherBoardSerialNo);
	            meter.setEthernetMac(ethMac);
	            try {
	                meterDao.saveMeter(meter);
	            } catch (Exception e) {
	                throw new Exception(e.getMessage());
	            }
	            if (meter.getMeterId() != null) {
	                response.put("meterId", meter.getMeterId().toString());
	                if (meter.getStatusId() != null && meter.getStatusId().equals(15L)) {
	                    Long hhid = jpaAssetInstallationDao.getHouseHoldIdByMeterId(meter.getMeterId());
	                    response.put("householdId", hhid != null ? hhid.toString() : "");
	                } else
	                    response.put("householdId", "");
	            } else {
	                response.put("meterId", "");
	                response.put("householdId", "");
	            }
	        }
	        return response;
	    }
	    private void setMeterStatusProductionDateValidFromAndCreatedTime(Meter meter) {
	        meter.setStatusId(16L);
	        meter.setCreatedTime(new Timestamp(System.currentTimeMillis()));
	        meter.setProductionDate(getProductionAndValidFromDate());
	        meter.setValidFrom(meter.getProductionDate());
	    }
	    private String getMeterVersion(String meterId, String hwVersion) {
	        String osVersion = null;
	        if (hwVersion != null) {
	            if (hwVersion.startsWith("2"))
	                osVersion = BM2;
	            if (hwVersion.startsWith("3"))
	                osVersion = BM3;
	            if (hwVersion.startsWith("4"))
	                osVersion = BM4;
	        } else {
	            if (meterId.startsWith("52") || meterId.startsWith("51"))
	                osVersion = BMA_SET_VERSION;
	            if (meterId.startsWith("54"))
	                osVersion = BMA_1_0_VERSION;
	            if (meterId.startsWith("1"))
	                osVersion = BM2;
	            if (meterId.startsWith("3"))
	                osVersion = BM3;
	            if (meterId.startsWith("4"))
	                osVersion = BM4;
	        }
	        return osVersion;
	    }
	    @Override
	    public String updateprintedmeterid(String deviceID, String meterId) throws Exception {
	        String meterIdUpdated = null;
	        Meter meter = null;

	        meter = meterService.fetchByDeviceId(deviceID);
	        if (meter != null) {
	            if (meter.getMeterId() == null || meter.getMeterId().equals(meter.getDeviceId())) {
	                meter.setMeterId(Long.valueOf(meterId));
	                setMeterStatusProductionDateValidFromAndCreatedTime(meter);
	                meter.setOsTypeId(getOsDataId(getMetaDataName(38L), getMeterVersion(meterId, null)));
	                try {
	                    meter = meterDao.saveOrUpdateMeter(meter, false, meter.getUpdatedBy());
	                } catch (Exception e) {
	                    throw new Exception(e.getMessage());
	                }
	            }
	            meterIdUpdated = meter.getMeterId().toString();
	        }
	        return meterIdUpdated;
	    }

	@Override
	public Map<String, Object> getallmembersfordevicebystatus(String status, String meterId) {
		return Map.of();
	}

	private static Map<String, Map<String, Long>> osTypeHWversionMap = new ConcurrentHashMap<>();

	    public static Long getOsDataId(String osType, String hwVersion) {
	        Long id = null;
	        if (osTypeHWversionMap.containsKey(osType)) {
	            Map<String, Long> hwVersionMap = osTypeHWversionMap.get(osType);
	            if (hwVersionMap.containsKey(hwVersion))
	                id = hwVersionMap.get(hwVersion);
	        }
	        return id;
	    }
}
