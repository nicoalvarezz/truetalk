package com.fyp.alethiaservice;

import com.fyp.alethiaservice.dto.users.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AlethiaServiceApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	private ObjectMapper mapper = new ObjectMapper();

	private static final String VALID_EMAIL = "nicoalvarezgarrido@gmail.com";
	private static final String VALID_PHONE_NUMBER = "+353123456789";

	@Test
	void testTriggerVerificationEndpoint() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/alethia/trigger-verification")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(
							getUserRequest(VALID_EMAIL, VALID_PHONE_NUMBER))))
				.andExpect(status().isOk());
	}

	private UserRequest getUserRequest(String email, String phoneNumber) {
		return UserRequest.builder()
				.email(email)
				.phoneNumber(phoneNumber)
				.build();
	}
}
