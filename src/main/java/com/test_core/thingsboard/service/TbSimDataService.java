package com.test_core.thingsboard.service;


import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.test_core.thingsboard.common.SimData;
import com.test_core.thingsboard.common.SimInfo;

@Service
public interface TbSimDataService {
	
    SimData save(SimInfo simInfo, BindingResult bindingResult) throws Exception;

    SimData saveSimData(String deviceID, String meterId, String imsi, String simSerial, String serviceProvider, String phoneNo, Long serviceProviderId) throws Exception;
}
