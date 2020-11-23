package com.taez.distrimoney.model;

import lombok.Data;

@Data
public class DistriMoneyRequest {
	String createUserID; 
	String targetRoomID; 
	int oriMoney;
	int targetUserCnt;
	int expiered;
}
