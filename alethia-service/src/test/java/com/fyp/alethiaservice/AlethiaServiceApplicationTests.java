package com.fyp.alethiaservice;

import com.fyp.alethiaservice.dto.users.UserRequest;
import com.fyp.hiveshared.api.helpers.ApiHelpers;
import com.fyp.hiveshared.api.responses.HiveResponseBody;
import org.junit.jupiter.api.Assertions;
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

import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andReturn();

		HiveResponseBody response = MAPPER.readValue(result.getResponse().getContentAsString(), HiveResponseBody.class);

		Assertions.assertEquals(response.getMessage(), "Verification link sent");
		Assertions.assertEquals(response.getMethod(), HttpStatus.OK);
		Assertions.assertEquals(response.getService(), SERVICE);
	}

	@Test
	void testTriggerVerificationLinkNotSent() throws Exception {
		performPost(TRIGGER_VERIFICATION_ENDPOINT,
										MAPPER.writeValueAsString(getUserRequest(INVALID_EMAIL, INVALID_PHONE_NUMBER)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest())
				.andReturn();
	}

	@Test
	void testIsAccessTokenExpired() {
		String invalidAccessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6Ijk4OTUxZjFmNGI0NDM0MGEyNzU1N2JlNmY0YzY0YTJjNjZlZTFmZWNhNDdlOTQyZTRjNDQ3Yzc2YzY0NTY0NjNkNDc4N2JiZDY3M2FkNGUwIn0.eyJhdWQiOiIxNTYwIiwianRpIjoiOTg5NTFmMWY0YjQ0MzQwYTI3NTU3YmU2ZjRjNjRhMmM2NmVlMWZlY2E0N2U5NDJlNGM0NDdjNzZjNjQ1NjQ2M2Q0Nzg3YmJkNjczYWQ0ZTAiLCJpYXQiOjE2NzA5NDExMjYsIm5iZiI6MTY3MDk0MTEyNiwiZXhwIjoxNjcxMDI3NTI1LCJzdWIiOiI1NTIyIiwic2NvcGVzIjpbXX0.KDz5bBPN60NkKR12aA4JQ3Omg0Egvy7o5iUpY5iD3I6u0sQZJf51Lzs1x4HCJaZRpuqtpAEf14xbm1_EC8U_MpffeJSGBxxXu3V0qFbR_UbSc7yxIu-Ik9ZQUuDNt3VlCRZESoD9M1VGp-MKPqgj5IKrA3e2DcZ7maul_K8DEvo_nm58U4Vlu_G8YFGRoH_WXBGr4Kz5aDd4zNdyNY2Nfd_Lkf1I7yHHU7bFnClWOSLGLAbOT2qinwJFIUzxsYsczj8g5-oPpWwkTOyEpKOpp1QqNnh3Py5jOwNuhvWlPnE6jfEVlxQpmbNlAoeVCTjZpxMYoYbAmWRkOdyXO0TfbGElEBY1G6s-F7H9jqp95E_0shpAfPDIXmgYlZXY9ixNd0cDavrtnFUYyqsQwm_8xvv-W5mNdFIKIZERbvX9VidToUviCGErUPw38aurlGc8kzSdsLrjy7RC1O9H55RUIe0j1HcUhH5WFPRW9c6JBAfBAuMDAsgpE2zIWvyKeRBcP6ccQjH4iCVvadpcadY-5nPmKNmbrrzZ9JDAYp1X_542jfXLrL6W8jy4IwBmtuScNhiU-6Ix_ORCSJvO4kc3r-Qhz5VIclBTT7iHZ94FXZbr-GpSLooyjvd7FG04oGD86DwJbTaF_Gv8V8hdaCHBVy0VNAHNmEHqFfC08REs3JY";
		Assertions.assertTrue(ApiHelpers.isAccessTokenExpired(invalidAccessToken));
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
