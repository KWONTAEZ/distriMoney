package com.taez.distrimoney.repository;

public interface DistriMoneyRepo {
	/**
	 * repository 에 해당 내용을 저장한다.
	 * @param key
	 * @param obj
	 */
	public void insertObject(String key, Object obj);
	
	public boolean containsKey(String key);
	
	public Object getObject(String key);
}
