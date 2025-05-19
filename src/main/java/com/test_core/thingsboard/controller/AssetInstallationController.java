package com.test_core.thingsboard.controller;

import java.net.BindException;
import java.util.HashMap;
import java.util.Map;

import com.test_core.thingsboard.customException.ThingsboardException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.test_core.thingsboard.service.*;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AssetInstallationController {

	@Autowired
	private AssetInstallationService service;

	@RequestMapping(value = "/checktvidPresent", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<Map<String, Object>> checkTVIdPresent(@RequestParam Long meterId) throws BindException {
		Integer tvId = service.checkTVIdPresent(meterId);
		Map<String, Object> response = new HashMap<>();
		if (tvId != null) {
			response.put("message", "data present");
			response.put("dataPresent", true);
			response.put("value", null);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			response.put("message", "data not present");
			response.put("dataPresent", false);
			response.put("value", null);
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/config/updatehouseholdid", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseEntity<String> updateConfigHhid(
			@RequestParam(required = true) Long houseHoldId,
	        @RequestParam(required = true) Long meterId) {
		String message = service.updateConfigHhid(houseHoldId, meterId);
		if (message != null)
			return new ResponseEntity<>(message, HttpStatus.OK);
		else
			return new ResponseEntity<>("Couldn't update Household Id.Please Try Again With Valid Parameter.",
					HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/getdevicedetails")
	public ResponseEntity<Map<String, Object>> getDeviceDetails(@RequestParam Long meterId) throws ThingsboardException {
		Map<String, Object> deviceData = service.getdevicedetails(meterId);
		if (deviceData != null && !deviceData.isEmpty())
			return new ResponseEntity<>(deviceData, HttpStatus.OK);
		else {
			deviceData = new HashMap<>();
			deviceData.put("message", "Device does not exist. Please try again with a valid device ID.");
			deviceData.put("success", false);
			return new ResponseEntity<>(deviceData, HttpStatus.BAD_REQUEST);
		}
	}
}
