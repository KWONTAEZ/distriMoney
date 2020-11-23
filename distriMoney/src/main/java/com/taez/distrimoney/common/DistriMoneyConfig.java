package com.taez.distrimoney.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.taez.distrimoney.repository.DistriMoneyRepo;
import com.taez.distrimoney.repository.DistriMoneyRepository;

@Configuration
public class DistriMoneyConfig {
	
	@Value("${distriMoney.expiered.type}")
	String expieredType;
	
	@Value("${distriMoney.expiered.value}")
	int expieredTime;
	
	@Value("${distriMoney.repository.size}")
	int repositorySize;

	@Bean
	public DistriMoneyRepo createRepository() {
		Map<String, Object> maps = new ConcurrentHashMap<String, Object>(repositorySize);
		DistriMoneyRepo repository = new DistriMoneyRepository(maps);
		return repository;
	}
	
}
