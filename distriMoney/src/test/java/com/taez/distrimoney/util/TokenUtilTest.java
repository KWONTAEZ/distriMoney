package com.taez.distrimoney.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class TokenUtilTest {
	
	/**
	 * token 생성 관련 테스트 
	 */
	@Test
	public void createTokenTest() {
		String token = TokenUtil.createToken();
		assertThat(token).isNotBlank();
		assertThat(token.length()).isEqualTo(3);
	}

}
