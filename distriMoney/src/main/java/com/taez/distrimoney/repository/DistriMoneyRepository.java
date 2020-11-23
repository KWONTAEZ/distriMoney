package com.taez.distrimoney.repository;

import java.util.Map;


public class DistriMoneyRepository implements DistriMoneyRepo {

	private Map<String, Object> distriMoneyStore;
	
	public DistriMoneyRepository(Map<String, Object> map) {
		this.distriMoneyStore = map;
	}

	@Override
	public void insertObject(String key, Object obj) {
		distriMoneyStore.put(key, obj);
	}

	@Override
	public boolean containsKey(String key) {
		return distriMoneyStore.containsKey(key);
	}

	@Override
	public Object getObject(String key) {
		return distriMoneyStore.get(key);
	}

}
