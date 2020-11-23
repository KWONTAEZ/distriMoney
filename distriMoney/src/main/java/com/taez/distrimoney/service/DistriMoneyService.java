package com.taez.distrimoney.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.taez.distrimoney.model.DistriMoneyInfo;
import com.taez.distrimoney.model.DistriMoneyRequest;
import com.taez.distrimoney.model.Response;
import com.taez.distrimoney.repository.DistriMoneyRepo;
import com.taez.distrimoney.util.DateUtil;
import com.taez.distrimoney.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DistriMoneyService {

	private static final int ERROR_NUM = -1;

	@Autowired
	DistriMoneyRepo distriMoneyrepo;

	public String createDistriMoneyInfo(DistriMoneyRequest request) {
		String token = null;
		DistriMoneyInfo info = new DistriMoneyInfo(request.getCreateUserID(), request.getTargetRoomID(),
				request.getOriMoney(), request.getTargetUserCnt(), 10);
		List<Integer> distriMoney = createDistriMoneys(request.getOriMoney(), request.getTargetUserCnt());
		info.setDistriMoney(distriMoney);
		token = TokenUtil.createToken();

		distriMoneyrepo.insertObject(token, info);

		return token;
	}

	public DistriMoneyInfo describeInfo(String token) {
		if (!distriMoneyrepo.containsKey(token)) {
			log.error("{}|{}| The token is wrong", token);
			return null;
		}

		return (DistriMoneyInfo) distriMoneyrepo.getObject(token);
	}

	public int takenMoney(String userId, String roomId, String token) {
		if (!distriMoneyrepo.containsKey(token)) {
			log.error("{}|{}| The token is wrong", userId, token);
			return ERROR_NUM;
		}
		return takenMoney((DistriMoneyInfo) distriMoneyrepo.getObject(token), userId, roomId);
	}

	public int takenMoney(DistriMoneyInfo info, String userId, String roomId) {

		long currentTime = DateUtil.createCurrentTime();

		if ((info.getCreateDate() + (10 * 60 * 1000)) < currentTime) {
			log.error("userId : {}| createDate : {}|The request has expired.", userId, info.getCreateDate());
			return ERROR_NUM;

		}
		if (userId.equals(info.getCreateUserID())) {
			log.error("userId : {}| createId: {}| It cannot be distributed with the created ID.", userId, info.getCreateUserID());
			return ERROR_NUM;
		}
		// 전체다 가져갔는지 중요
		if (info.getDistriTargetUserCnt() == info.getGetUserCnt()) {
			log.error("userId : {}| All the money has been distributed", userId);
			return ERROR_NUM;
		}
		if (!info.getTargetRoomID().equals(roomId)) {
			log.error("userId : {}|roomId: {}| this room numver is wrong", userId, roomId);
			return ERROR_NUM;
		}
		if (info.getTargetUserInfos().containsKey(userId)) {
			log.error("userId : {}| This user Id is wrong", userId);
			return ERROR_NUM;
		}
		int distriMoney = info.takeMoney();
		info.getTargetUserInfos().put(userId, distriMoney);
		return distriMoney;
	}

	private List<Integer> createDistriMoneys(int oriMoney, int targetUserCnt) {
		List<Integer> targetDistriMoney = new ArrayList<Integer>();
		int per = 0;
		int divMoney = 0;
		do {
			per = ThreadLocalRandom.current().nextInt(0, 100);
			divMoney = (oriMoney * per) / 100;
			oriMoney -= divMoney;
			targetUserCnt--;
			targetDistriMoney.add(divMoney);
		} while (targetUserCnt != 1);
		targetDistriMoney.add(oriMoney);
		return targetDistriMoney;
	}

}
