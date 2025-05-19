package com.test_core.thingsboard.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.test_core.thingsboard.repo.MeterHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test_core.thingsboard.entity.Meter;
import com.test_core.thingsboard.entity.MeterEntity;
import com.test_core.thingsboard.entity.MeterHistoryEntity;
import com.test_core.thingsboard.repo.MeterRepository;
	
@Service
public class MeterDao {
	
	@Autowired
	MeterRepository meterRepository;
	
	@Autowired
    MeterHistoryRepository meterHistoryRepository;
	
    public Meter findByDeviceId(String deviceId) {
        return DaoUtil.getData(meterRepository.findByDeviceId(deviceId));
    }
    
    public Meter saveMeter(Meter meter) {
        return DaoUtil.getData(meterRepository.save(new MeterEntity(meter)));
    }

    public String getdevicelicensekey(String deviceId) {
        return meterRepository.findLicensekeyByDeviceId(deviceId);
    }

    private void saveMeterHistoryEntity(Long meterId, Long statusId, String remark, Timestamp timestamp,
			String userName) {
		MeterHistoryEntity meterHistoryEntity = new MeterHistoryEntity();
		meterHistoryEntity.setMeterId(meterId);
		meterHistoryEntity.setValidFrom(timestamp);
		meterHistoryEntity.setStatusId(statusId);
		meterHistoryEntity.setRemark(remark);
		meterHistoryEntity.setCreatedBy(userName);
		meterHistoryRepository.save(meterHistoryEntity);
	}

	public void saveOrUpdateMeterHistoryEntity(boolean isUpdate, String userName, Long meterId, Long statusId,
			String remark) throws Exception {
		try {
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			if (isUpdate) {
				MeterHistoryEntity existingMeterHistoryEntity = meterHistoryRepository
						.findLatestMeterHistoryEntity(meterId);
				if (existingMeterHistoryEntity != null) {
					existingMeterHistoryEntity.setUpdatedBy(userName);
					existingMeterHistoryEntity.setValidTo(timestamp);
					meterHistoryRepository.save(existingMeterHistoryEntity);
				}
			}
			saveMeterHistoryEntity(meterId, statusId, remark, timestamp, userName);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public Meter saveOrUpdateMeter(Meter meter, boolean isUpdate, String userName) throws Exception {
		try {
			Meter meterData = DaoUtil.getData(meterRepository.save(new MeterEntity(meter)));
			if (meterData != null) {
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				if (isUpdate) {
					MeterHistoryEntity existingMeterHistoryEntity = meterHistoryRepository
							.findLatestMeterHistoryEntity(meterData.getMeterId());
					if (existingMeterHistoryEntity != null) {
						existingMeterHistoryEntity.setUpdatedBy(meterData.getUpdatedBy());
						existingMeterHistoryEntity.setValidTo(timestamp);
						meterHistoryRepository.save(existingMeterHistoryEntity);
					}
				}
				saveOrUpdateMeterHistoryEntity(isUpdate, userName, meter.getMeterId(), meter.getStatusId(),
						meter.getRemark());
				return meterData;
			} else
				throw new Exception("Error Occurred while saving or updating the Meter data");
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	public Map<String, Object> getallmembersfordevicebystatus(String status, String meterId) {
		Map<String, Object> meterMembersdata = null;
		List<Object[]> results = meterRepository.findLatestMeterMembersdata(Long.valueOf(meterId));
		if (!results.isEmpty()) {
			meterMembersdata = new HashMap<>();
			Map<String, Object> devicedetails = new HashMap<>();
			MeterEntity meterEntity = results.get(0)[0] != null ? (MeterEntity) results.get(0)[0] : null;
			if (meterEntity != null) {
				devicedetails.put("deviceID", meterEntity.getDeviceId());
				devicedetails.put("app_Id", meterEntity.getDeviceId());
				devicedetails.put("longitude", meterEntity.getLongitude());
				devicedetails.put("latitude", meterEntity.getLatitude());
				devicedetails.put("meterId", meterEntity.getMeterId());
				devicedetails.put("lastOnline", meterEntity.getLastConnectedDate());
				devicedetails.put("assetSerialNumber", meterEntity.getAssetSerialNumber());
				devicedetails.put("deviceSerialNumber", meterEntity.getDeviceSerialNumber());
				devicedetails.put("licenseKey", meterEntity.getKLicenceKey());
				devicedetails.put("appVersion", meterEntity.getSwVersion());
				devicedetails.put("printed_meterid", meterEntity.getMeterId());
				devicedetails.put("imeiNumber1", meterEntity.getImeiNumber1());
				devicedetails.put("imeiNumber2", meterEntity.getImeiNumber2());
				devicedetails.put("simOperator", meterEntity.getSimOperator());
				devicedetails.put("networkOperator", meterEntity.getNetworkOperator());
				devicedetails.put("networkOperatorName", meterEntity.getNetworkOperatorName());
			}
			devicedetails.put("osVersion", results.get(0)[4] != null ? results.get(0)[4] : null);
			devicedetails.put("simSerialNumber", results.get(0)[5] != null ? results.get(0)[5] : null);
			devicedetails.put("simOperatorName", results.get(0)[6] != null ? results.get(0)[6] : null);
//			devicedetails.put("syncFrequency", meterConfigDao.findTransmissionFreqDataByMeterId(Long.valueOf(meterId)));
			devicedetails.put("houseHoldId", results.get(0)[2] != null ? results.get(0)[2].toString() : null);
			devicedetails.put("tvId", results.get(0)[1] != null ? results.get(0)[1].toString() : null);
			devicedetails.put("meterBeep", false);
			meterMembersdata.put("devicedetails", devicedetails);
			if (results.get(0)[3] != null) {
				List<String> members = results.get(0)[3] != null ? (List<String>) results.get(0)[3] : null;
				List<Map<String, Object>> memebersDataList = null;
				if (members != null && !members.isEmpty()) {
					memebersDataList = new ArrayList<>();
					String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
					for (int i = 0; i < members.size(); i++) {
						Map<String, Object> memberData = new HashMap<>();
						memberData.put("name", members.get(i));
						memberData.put("status", "ACTIVE");
						memberData.put("key", alphabet.charAt(i));
						memebersDataList.add(memberData);
					}

				}
				meterMembersdata.put("memberlist", memebersDataList);
			}
			meterMembersdata.put("success", true);
		}
		return meterMembersdata;
	}
    
}
