package com.test_core.thingsboard.controller;

import com.test_core.thingsboard.customException.ThingsboardException;
import com.test_core.thingsboard.service.TbMeterOTADataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

public class MeterOTADataController {

    @Autowired
    private TbMeterOTADataService defaulMeterOTADataService;

    //	   @ApiOperation(value = "Get latest build file status")
    @RequestMapping(value = "/getlatestbuildfilestatus", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity getlatestbuildfilestatus(@RequestParam Long meterId) throws ThingsboardException {
        Map<String, Object> response = defaulMeterOTADataService.getlatestbuildfilestatus(meterId);
        if (response.isEmpty()) {
            response.put("status", "No data found");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } else
            return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
