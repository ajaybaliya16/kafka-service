package com.test_core.thingsboard.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.test_core.thingsboard.customException.ThingsboardException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test_core.thingsboard.service.TbMeterService;
@RestController
//@TbCoreComponent
@RequestMapping("/api")
//@RequiredArgsConstructor
public class MeterController {


//    private final MeterService meterService;
    
//    private final
	@Autowired
    TbMeterService tbMeterService;

//    @Autowired
//    public MeterController(MeterService meterService,TbMeterService tbMeterService) {
//        this.meterService = meterService;
//        this.tbMeterService=tbMeterService;
//    }
	
//    @ApiOperation(value = "Signup Device", notes = "Signup Device.")
    @PostMapping(value = "/signupdevice")
    public ResponseEntity signupdevice(@RequestParam("deviceID") String deviceID,
                                       @RequestParam("lat") Double lat,
                                       @RequestParam("lng") Double lng,
                                       @RequestParam("meterId") String meterId,
                                       @RequestParam("deviceSerialNumber") String deviceSerialNumber,
                                       @RequestParam("assetSerialNumber") String assetSerialNumber,
                                       @RequestParam("simSerialNumber") String simSerialNumber,
                                       @RequestParam("imeiNumber1") String imeiNumber1,
                                       @RequestParam("imeiNumber2") String imeiNumber2,
                                       @RequestParam("simOperator") String simOperator,
                                       @RequestParam("networkOperator") String networkOperator,
                                       @RequestParam("networkOperatorName") String networkOperatorName,
                                       @RequestParam("osVersion") String osVersion,
                                       @RequestParam("appVersion") String appVersion,
                                       @RequestParam("simOperatorName") String simOperatorName,
                                       @RequestParam(value = "EthernetMAC", required = false) String ethMac,
                                       @RequestParam(value = "MotherBoardSNo", required = false) String motherBoardSerialNo,
                                       @RequestParam(value = "IMSI", required = false) String imsi) throws Exception {
        if (deviceID == null) {
//            log.error("Invalid request: DeviceID should not be null or empty");
            throw new Exception("Invalid request: deviceID should not be null or empty ");
        }
        tbMeterService.signupdevice(deviceID, lat, lng, meterId, deviceSerialNumber, assetSerialNumber, simSerialNumber, imeiNumber1, imeiNumber2, simOperator, networkOperator, networkOperatorName, osVersion, appVersion, ethMac, motherBoardSerialNo, imsi, simOperatorName);
        return new ResponseEntity<>("Device created", HttpStatus.OK);
    }

//    @ApiOperation(value = "Get Device Licence Key", notes = "Get Device Licence Key.")
    @GetMapping(value = "/getdevicelicensekey")
    public ResponseEntity getdevicelicensekey(@RequestParam("deviceID") String deviceID) throws Exception {
        if (deviceID == null) {
//            log.error("Invalid request: DeviceID should not be null or empty");
            throw new Exception("Invalid request: DeviceID should not be null or empty ");
        }
        String licenseKey = tbMeterService.getdevicelicensekey(deviceID);
        Map<String, Object> response = new HashMap<>();
        if (licenseKey != null) {
            response.put("license", licenseKey);
            response.put("success", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", false);
            response.put("message", "Device does not exists. Please Try Again With Valid Device ID.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

//    @ApiOperation(value = "Generate Device License key", notes = "Generate Device License key.")
    @GetMapping(value = "/generatedevicelicensekey")
    public ResponseEntity generatedevicelicensekey(@RequestParam("deviceID") String deviceID) throws Exception {
        if (deviceID == null) {
//            log.error("Invalid request: DeviceID should not be null or empty");
            throw new Exception("Invalid request: DeviceID should not be null or empty ");//, ThingsboardErrorCode.BAD_REQUEST_PARAMS);
        }
        String licenseKey = tbMeterService.generatedevicelicensekey(deviceID);
        Map<String, Object> response = new HashMap<>();
        if (licenseKey != null) {
            response.put("license", licenseKey);
            response.put("success", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", false);
            response.put("message", "Device does not exists. Please Try Again With Valid Device ID.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
	@PostMapping(value = "/saveapplatestversion")
	public ResponseEntity saveapplatestversion(@RequestParam("appVersion") String appVersion,
			@RequestParam("appUpdatedDate") String appUpdatedDate, @RequestParam("deviceID") String deviceID)
			throws Exception {

		if (Strings.isEmpty(deviceID) || Strings.isEmpty(appUpdatedDate) || Strings.isEmpty(appVersion)) {
			throw new Exception(
					"Invalid request: DeviceID, AppVersion and AppUpdatedDate should not be null or empty "
					);

		}
		String version = tbMeterService.saveapplatestversion(deviceID, appVersion, appUpdatedDate);
		Map<String, Object> response = new HashMap<>();
		if (version != null) {
			response.put("version", version);
			response.put("success", true);
			response.put("Last Updated Date", LocalDateTime.now().toString());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put("success", true);
			response.put("message", "Device does not exists. Please Try Again With Valid Device ID.");
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}
    @GetMapping(value = "/getmeterid")
    public ResponseEntity getmeterid(@RequestParam("MotherBoardSNo") String motherBoardSerialNo, @RequestParam("EthernetMAC") String ethMac, @RequestParam("deviceID") String deviceID) throws Exception {
        if (Strings.isEmpty(deviceID) || Strings.isEmpty(motherBoardSerialNo) || Strings.isEmpty(ethMac)) {
            throw new Exception("Invalid request: DeviceID, EthMac and  MotherBoardSerialNo not be null or empty ");
        }
        Map<String, Object> response = tbMeterService.getmeterid(deviceID, motherBoardSerialNo, ethMac);
        if (!response.isEmpty()) {
            response.put("success", true);
            response.put("currentTimestamp", LocalDateTime.now().toString());
            response.put("currentTimestampInMillis", System.currentTimeMillis());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", true);
            response.put("currentTimestamp", LocalDateTime.now().toString());
            response.put("message", "Ethernet MAC address or MotherboardSerial No or DeviceID does not exists. Please Try Again With Valid Values.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    @PutMapping(value = "/updateprintedmeterid")
    public ResponseEntity updateprintedmeterid(@RequestParam("deviceID") String deviceID, @RequestParam("printedMeterId") String printedMeterId) throws Exception {
        if (Strings.isEmpty(deviceID) || Strings.isEmpty(printedMeterId)) {
            throw new Exception("Invalid request: DeviceID, PrintedMeterId should not be null or empty ");
        }
        String updateMeterid = tbMeterService.updateprintedmeterid(deviceID, printedMeterId);
        Map<String, Object> response = new HashMap<>();
        if (updateMeterid != null) {
            response.put("printed meterid", updateMeterid);
            response.put("success", true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("success", false);
            response.put("currentTimestamp", LocalDateTime.now().toString());
            response.put("message", "Device does not exists. Please Try Again With Valid Device ID.");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping(value = "/getallmembersfordevicebystatus", produces = "application/json")
    public ResponseEntity<?> getAllMembersForDeviceByStatus(
            @RequestParam String status,
            @RequestParam String meterId) throws ThingsboardException {

        if (Strings.isEmpty(status) || Strings.isEmpty(meterId)) {
            throw new ThingsboardException();
            //("Invalid request: Status or MeterId should not be null or empty ");
        }

        Map<String, Object> response = tbMeterService.getallmembersfordevicebystatus(status, meterId);

        if (response != null && !response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
}
