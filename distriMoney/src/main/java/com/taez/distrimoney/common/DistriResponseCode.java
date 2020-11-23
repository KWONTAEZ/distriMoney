package com.taez.distrimoney.common;

public enum DistriResponseCode {

	SUCCESS(200,"SUCCESS"),
	INVALIDE_TOKEN(300,"INVALIDE_TOKEN");
	
	private int code;
	private String message;
	private DistriResponseCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public int code() {
		return this.code;
	}
	
	public String message() {
		return this.message;
	}
	
}
