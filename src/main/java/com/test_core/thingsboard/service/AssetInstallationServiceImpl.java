package com.test_core.thingsboard.service;

import static com.test_core.thingsboard.dao.DataUtils.getMetaDataName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test_core.thingsboard.dao.AssetInstallationRepository;


@Service
public class AssetInstallationServiceImpl implements AssetInstallationService {
	

	
	@Autowired
	AssetInstallationRepository assetInstallationRepository;
	
	//3
	@Override
	public Integer checkTVIdPresent(Long meterId) {
		 List meterIdTVIdList = assetInstallationRepository.findMeterIdTVIdAndHhid(meterId);
	        if (meterIdTVIdList != null && !meterIdTVIdList.isEmpty()) {
	            Integer tvId = Integer.valueOf(((List) meterIdTVIdList.get(0)).get(1).toString());
	            Long meterStatus = Long.valueOf(((List) meterIdTVIdList.get(0)).get(2).toString());
	            if (meterStatus.equals(15L) || meterStatus.equals(24L))
	                return tvId;
	        }
	        return null;
	}
	
	//2
	  @Override
	    public String updateConfigHhid(Long hhid, Long meterId) {
	        List meterIdHhidList = assetInstallationRepository.findMeterIdTVIdAndHhid(meterId);
	        if (meterIdHhidList != null && !meterIdHhidList.isEmpty()) {
	            Long hhidData = Long.valueOf(((List) meterIdHhidList.get(0)).get(0).toString());
	            String meterStatus = getMetaDataName(Long.valueOf(((List) meterIdHhidList.get(0)).get(2).toString()));
	            Long meterStatusId = Long.valueOf(((List) meterIdHhidList.get(0)).get(2).toString());
	            if (meterStatusId.equals(15L) || meterStatusId.equals(24L)) {
	                String meterStatusData = meterStatusId.equals(15L) ? "installed" : "installation in progress";
	                if (hhid.equals(hhidData))
	                    return "Meter already " + meterStatusData + " with the same household";
	                else
	                    return "Meter already " + meterStatusData + " with the different household. Hhid is " + hhidData;
	            } else if (meterStatusId.equals(16L))
	                return "Meter is free without any household";
	        }
	        return null;
	    }

	@Override
	public Map<String, Object> getdevicedetails(Long meterId) {
			List meterIdTVIdAndHhidList = assetInstallationRepository.findMeterIdTVIdAndHhid(meterId);
			if (meterIdTVIdAndHhidList != null && !meterIdTVIdAndHhidList.isEmpty()) {
				Long meterStatusId = Long.valueOf(((List) meterIdTVIdAndHhidList.get(0)).get(2).toString());
				Map<String, Object> deviceDetails = new HashMap<>();
				if (meterStatusId.equals(15L) || meterStatusId.equals(24L)) {
					deviceDetails.put("hhid", Long.valueOf(((List) meterIdTVIdAndHhidList.get(0)).get(0).toString()));
					deviceDetails.put("tvId", Long.valueOf(((List) meterIdTVIdAndHhidList.get(0)).get(1).toString()));
				} else if (meterStatusId.equals(16L)) {
					deviceDetails.put("hhid", 0);
					deviceDetails.put("tvId", 0);
				}
				return deviceDetails;
			}
			return null;
		}
	}


//--- Services: DefaultAssetInstallationService extends AbstractTbEntityService implements TbAssetInstallationService
//
//--- Method inside DefaultAssetInstallationService:
//	 public Map<String, Object> getdevicedetails(Long meterId) {
//   	    return assetInstallationService.getdevicedetails(meterId);
//   	}
//
//--- Method inside assetInstallationService :
//	public Map<String, Object> getdevicedetails(Long meterId) {
//       	return assetInstallationDao.getdevicedetails(meterId);
//   	}