package com.fyp.alethiaservice;

import com.fyp.alethiaservice.dto.users.UserRequest;
import com.fyp.hiveshared.api.response.ApiResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlethiaServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private static final ObjectMapper MAPPER = new ObjectMapper();

	private static final String VALID_EMAIL = "somerandomemail@gmail.com";
	private static final String VALID_PHONE_NUMBER = "+3530838455233";
	private static final String INVALID_EMAIL = "someinvalidemail";
	private static final String INVALID_PHONE_NUMBER = "353123456782232323232323";

	private static final String SERVICE = "alethia-service";
	private static final String TRIGGER_VERIFICATION_ENDPOINT = "/api/alethia/trigger-verification";

	@Test
	void testTriggerVerificationLinkSent() throws Exception {
		MvcResult result = performPost(TRIGGER_VERIFICATION_ENDPOINT,
										MAPPER.writeValueAsString(getUserRequest(VALID_EMAIL, VALID_PHONE_NUMBER)))
				.andExpect(status().isOk())
				.andReturn();

		ApiResponse response = MAPPER.readValue(result.getResponse().getContentAsString(), ApiResponse.class);

		assertEquals(response.getMessage(), "Verification link sent");
		assertEquals(response.getMethod(), HttpStatus.OK);
		assertEquals(response.getService(), SERVICE);
	}

	@Test
	void testTriggerVerificationLinkNotSent() throws Exception {
		performPost(TRIGGER_VERIFICATION_ENDPOINT,
										MAPPER.writeValueAsString(getUserRequest(INVALID_EMAIL, INVALID_PHONE_NUMBER)))
				.andExpect(status().isBadRequest())
				.andReturn();
	}

	private UserRequest getUserRequest(String email, String phoneNumber) {
		return UserRequest.builder()
				.email(email)
				.phoneNumber(phoneNumber)
				.build();
	}

	private ResultActions performPost(String endpoint, String content) throws Exception {
		return mockMvc.perform(MockMvcRequestBuilders.post(endpoint)
				.contentType(MediaType.APPLICATION_JSON)
				.content(content)
		);
	}
}
