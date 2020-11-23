package com.taez.distrimoney.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taez.distrimoney.util.DateUtil;

import lombok.Data;

/**
 * 돈 뿌리기 정보 해당 모델의 Key값은 token 이다.
 * 
 * @author taez
 *
 */
@Data
public class DistriMoneyInfo {

	public DistriMoneyInfo(String createUserID, String targetRoomID, int oriMoney, int distriTargetUserCnt, int expiered) {
		this.createUserID = createUserID;
		this.targetRoomID = targetRoomID;
		this.oriMoney = oriMoney;
		this.distriTargetUserCnt = distriTargetUserCnt;
		this.getUserCnt = 0;
		this.createDate = DateUtil.createCurrentTime();
		this.distriMoneyResult = 0;
		this.targetUserInfos = new HashMap<>(distriTargetUserCnt);
	}

	/**
	 * 돈 뿌리기 생성 아이디
	 */
	String createUserID;

	/**
	 * 돈 뿌리기 생성된 방 아이디
	 */
	String targetRoomID;

	/**
	 * 생성 시간
	 */
	long createDate;

	/**
	 * 돈 뿌리기 초기 금액
	 */
	int oriMoney;

	/**
	 * 방장을 제외한 방에 속한 사용자 수
	 */
	int distriTargetUserCnt;

	/**
	 * 돈 뿌리기에 대해서 몇명이 해당 뿌리기를 가져갔는지...
	 */
	int getUserCnt;

	int distriMoneyResult;

	List<Integer> distriMoney;

	// 지급 내역이 필요하다. 결과가 보고 싶으니까...
	// 내역은 별도로 저장한다.
	Map<String, Integer> targetUserInfos;
	
	public int takeMoney() {
		int disMoney = distriMoney.get(getUserCnt);
		this.getUserCnt++;
		return disMoney;
	}
	public boolean isExpiered() {
		return false;
	}
	
	public boolean isGetMoney(String userId) {
		return false;
	}
	
}
