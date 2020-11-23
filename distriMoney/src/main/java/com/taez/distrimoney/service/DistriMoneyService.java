package com.taez.distrimoney.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taez.distrimoney.model.DistriMoneyInfo;
import com.taez.distrimoney.model.DistriMoneyRequest;
import com.taez.distrimoney.repository.DistriMoneyRepo;
import com.taez.distrimoney.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DistriMoneyService {
	
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

	public int takenMoney(DistriMoneyInfo info, String userId, String roomId) {
		if (info.getCreateUserID().equals(userId)) {
			return -1;
		}
		if(!info.getTargetRoomID().equals(roomId)) {
			return -1;
		}
		if (info.getTargetUserInfos().containsKey(userId)) {
			return -1;
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
