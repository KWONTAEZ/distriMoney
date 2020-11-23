package com.taez.distrimoney.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.taez.distrimoney.model.DistriMoneyInfo;
import com.taez.distrimoney.model.DistriMoneyRequest;
import com.taez.distrimoney.repository.DistriMoneyRepo;
import com.taez.distrimoney.util.TokenUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class DistriMoneyServiceTest {

	@Autowired
	DistriMoneyService distriMoneyservice;
	
	@Autowired
	DistriMoneyRepo repository;
	
	/**
	 *  {@link DistriMoneyInfo} 생성 관련 테스트 
	 */
	@Test
	public void createDistriMoneyInfoTest() {
		String userId = "user_12345";
		String roomId = "room_12345";
		int oriMoney = 10000;
		int userCnt = 4;
		
		DistriMoneyRequest request = new DistriMoneyRequest();
		request.setCreateUserID(userId);
		request.setTargetRoomID(roomId);
		request.setOriMoney(oriMoney);
		request.setTargetUserCnt(userCnt);
		
		DistriMoneyInfo info = distriMoneyservice.createDistriMoneyInfo(request);
		assertThat((info.getDistriMoney().size())).isEqualTo(userCnt);
	}
	
	@Test
	public void takenMoneyTest() {
		String createId = "user_12345";
		String userId = "auser";
		String roomId = "room_12345";
		int oriMoney = 10000;
		int userCnt = 4;
		
		DistriMoneyRequest request = new DistriMoneyRequest();
		request.setCreateUserID(createId);
		request.setTargetRoomID(roomId);
		request.setOriMoney(oriMoney);
		request.setTargetUserCnt(userCnt);
		
		DistriMoneyInfo info = distriMoneyservice.createDistriMoneyInfo(request);
		String token = TokenUtil.createToken();
		repository.insertObject(token, info);
		
		int money = distriMoneyservice.takenMoney(info, userId, roomId);
		
		assertThat(money).isGreaterThan(0);
	}
	
	@Test
	public void createUser_takenMoneyTest() {
		String createId = "user_12345";
		String userId = "auser";
		String roomId = "room_12345";
		int oriMoney = 10000;
		int userCnt = 4;
		
		DistriMoneyRequest request = new DistriMoneyRequest();
		request.setCreateUserID(createId);
		request.setTargetRoomID(roomId);
		request.setOriMoney(oriMoney);
		request.setTargetUserCnt(userCnt);
		
		DistriMoneyInfo info = distriMoneyservice.createDistriMoneyInfo(request);
		String token = TokenUtil.createToken();
		repository.insertObject(token, info);
		
		int money = distriMoneyservice.takenMoney(info, createId, roomId);
		assertThat(money).isEqualTo(-1);
	}
	
	@Test
	public void duplication_request_takenMoneyTest() {
		String createId = "user_12345";
		String userId = "auser";
		String roomId = "room_1";
		int oriMoney = 10000;
		int userCnt = 4;
		
		DistriMoneyRequest request = new DistriMoneyRequest();
		request.setCreateUserID(createId);
		request.setTargetRoomID(roomId);
		request.setOriMoney(oriMoney);
		request.setTargetUserCnt(userCnt);
		
		DistriMoneyInfo info = distriMoneyservice.createDistriMoneyInfo(request);
		String token = TokenUtil.createToken();
		repository.insertObject(token, info);
		
		int money = distriMoneyservice.takenMoney(info, userId, roomId);
		money = distriMoneyservice.takenMoney(info, userId, roomId);
		assertThat(money).isEqualTo(-1);
		roomId = "room_2";
		money = distriMoneyservice.takenMoney(info, userId, roomId);
		assertThat(money).isEqualTo(-1);
	}
	
	
}
