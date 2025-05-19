package com.test_core.thingsboard.service;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface AssetInstallationService {

	public Integer checkTVIdPresent(Long meterId);
	public String updateConfigHhid(Long hhid, Long meterId) ;
	Map<String, Object> getdevicedetails(Long meterId);
}
