package com.taez.distrimoney.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.taez.distrimoney.model.DistriMoneyInfo;
import com.taez.distrimoney.model.DistriMoneyRequest;
import com.taez.distrimoney.model.Response;
import com.taez.distrimoney.repository.DistriMoneyRepo;
import com.taez.distrimoney.service.DistriMoneyService;
import com.taez.distrimoney.util.DateUtil;
import com.taez.distrimoney.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/dmoney")
public class DistriMoneyController {

	@Autowired
	DistriMoneyService distriMoneyService;

	@Autowired
	DistriMoneyRepo distriMoneyrepo;

	// token 생성
	@PostMapping("/create")
	public ResponseEntity<Response> distriMoney(@RequestHeader(value = "X-USER-ID") String userId,
			@RequestHeader(value = "X-ROOM-ID") String roomId, @RequestParam("money") int oriMoney,
			@RequestParam("userCnt") int userCnt) {
		DistriMoneyRequest request = new DistriMoneyRequest();
		request.setCreateUserID(userId);
		request.setTargetRoomID(roomId);
		request.setOriMoney(oriMoney);
		request.setTargetUserCnt(userCnt);
		String token = distriMoneyService.createDistriMoneyInfo(request);

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("token", token);
		return ResponseEntity.ok(new Response("SUCCESS", null, resultMap));
	}

	@PostMapping("/takeMoney")
	public ResponseEntity<Response> takeMoney(@RequestHeader(value = "X-USER-ID") String userId,
			@RequestHeader(value = "X-ROOM-ID") String roomId, @RequestParam(value = "token") String token) {
		String errMsg = null;
		String code = "SUCCESS";
		if (!distriMoneyrepo.containsKey(token)) {
			code = "ERROR";
			errMsg = "token is invalid.";
			return ResponseEntity.badRequest().body(new Response(code, errMsg, null));
		}
		DistriMoneyInfo info = (DistriMoneyInfo) distriMoneyrepo.getObject(token);
		// 가져간 계정인지 중요하다
		long currentTime = DateUtil.createCurrentTime();
		if ((info.getCreateDate() + (10 * 60 * 1000)) < currentTime) {
			code = "ERROR";
			errMsg = "The request has expired.";
			return ResponseEntity.badRequest().body(new Response(code, errMsg, null));
		}
		if (userId.equals(info.getCreateUserID())) {
			code = "ERROR";
			errMsg = "It cannot be distributed with the created ID.";
			return ResponseEntity.badRequest().body(new Response(code, errMsg, null));
		}
		// 전체다 가져갔는지 중요
		if (info.getDistriTargetUserCnt() == info.getGetUserCnt()) {
			code = "ERROR";
			errMsg = "All the money has been distributed.";
			return ResponseEntity.badRequest().body(new Response(code, errMsg, null));
		}
		int distriMoney = distriMoneyService.takenMoney((DistriMoneyInfo) distriMoneyrepo.getObject(token), userId,
				roomId);
		Map<String, Object> resultMap = new HashMap<>();

		if (distriMoney < 0) {
			code = "ERROR";
			errMsg = "takenMoney service error";
			return ResponseEntity.badRequest().body(new Response(code, errMsg, null));
		}
		resultMap.put("distriMoney", distriMoney);

		return ResponseEntity.ok(new Response(code, errMsg, resultMap));
	}

	@GetMapping("/describe")
	public ResponseEntity<Response> descriveDistriMoney(@RequestHeader(value = "X-USER-ID") String userId,
			@RequestHeader(value = "X-ROOM-ID") String roomId, @RequestParam(value = "token") String token) {
		String errMsg = null;
		String code = "SUCCESS";
		DistriMoneyInfo info = null;
		if (!distriMoneyrepo.containsKey(token)) {
			code = "ERROR";
			errMsg = "token error";
			return ResponseEntity.badRequest().body(new Response(code, errMsg, null));
		} else {
			info = (DistriMoneyInfo) distriMoneyrepo.getObject(token);
		}

		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("info", info);

		return ResponseEntity.ok(new Response(code, errMsg, resultMap));
	}

}
