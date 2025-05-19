package com.test_core.thingsboard.service;

import java.util.Map;

public interface TbMeterOTADataService {
    Map<String, Object> getlatestbuildfilestatus(Long meterId);
}
