package com.taez.distrimoney.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DistriMoneyControllerTest {
	
	@Autowired
	private WebApplicationContext wac;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private MockMvc mvc;
	
	@Before
	public void setup() {
		DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(this.wac);
		this.mvc = builder.build();
	}
	
	@Test
	public void distriMoneyTest() throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("money", "10000");
		params.put("userCnt", "4");
		String content = objectMapper.writeValueAsString(params);
		
		ResultActions actions = mvc.perform(post("/dmoney/create")
				.contentType(MediaType.APPLICATION_JSON)
				.header("X-USER-ID","create-user")
				.header("X-ROOM-ID", "room_1")
				.content(content));
		
		actions.andExpect(MockMvcResultMatchers.status().isOk());
		
		
	}
	
}
