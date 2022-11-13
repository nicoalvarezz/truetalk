package com.fyp.alethiaservice;

import com.fyp.alethiaservice.dto.idpal.IdpalWebhookRequest;
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
							getUserRequest(VALID_EMAIL, VALID_PHONE_NUMBER)))
					)
				.andExpect(status().isOk());
	}

	@Test
	void testWebhookReceiverEndpoint() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/api/alethia//webhook-receiver")
					.contentType(MediaType.APPLICATION_JSON)
					.content(mapper.writeValueAsString(
							getIdpalWebhookRequest()))
					)
				.andExpect(status().isOk());
	}

	private UserRequest getUserRequest(String email, String phoneNumber) {
		return UserRequest.builder()
				.email(email)
				.phoneNumber(phoneNumber)
				.build();
	}

	// TODO:
	// Create a method to test webhook request endpoint
	// Here is some test data
	// {
	//    "event_id": 13744738,
	//    "event_type": "cdd_complete",
	//    "uuid": "166d396b",
	//    "submission_id": 2159737,
	//    "source": "mobileapp"
	//}

	private IdpalWebhookRequest getIdpalWebhookRequest() {
		return IdpalWebhookRequest.builder()
				.eventId()
				.eventType()
				.uuid()
				.submissionId()
				.source()
				.build();
	}
}
