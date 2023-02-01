package com.fyp.alethiaservice;

import com.fyp.alethiaservice.dto.idpal.IdpalAccessToken;
import com.fyp.alethiaservice.dto.idpal.IdpalRenewAccessToken;
import com.fyp.alethiaservice.dto.idpal.IdpalRequest;
import com.fyp.alethiaservice.dto.idpal.IdpalWebhookRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
public class IdpalDtoTests {

    @Mock
    private IdpalRequest idpalRequestMock;

    @Mock
    private IdpalWebhookRequest idpalWebhookRequestMock;

    @Mock
    private IdpalAccessToken idpalAccessTokenMock;

    @Mock
    private IdpalRenewAccessToken idpalRenewAccessTokenMock;


    private static final String CLIENT_KEY = "fake_client_key";
    private static final String ACCESS_KEY = "fake_access_key";
    private static final String CLIENT_ID = "fake_client_id";
    private static final String INFORMATION_TYPE = "fake_information_type";
    private static final String CONTACT = "fake_conact";
    private static final int PROFILE_ID = 1;
    private static final int SUBMISSION_ID = 1;
    private static final String CONTENT_DISPOSITION = "fake_content_disposition";
    private static final int WEBHOOK_EVENT_ID = 15502526;
    private static final String WEBHOOK_EVENT_TYPE = "cdd_complete";
    private static final String WEBHOOK_UUID = UUID.randomUUID().toString();
    private static final int WEBHOOK_SUBMISSION_ID = 2527638;
    private static final String WEBHOOK_SOURCE = "mobileapp";
    private static final String IDPAL_ACCESS_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImQ1ZmUxZDI2ZTUzMTlmZWVhNTZkZGFkMzk3NzFjOTQwZTkyZjRmZmFkZWJmNmYwYjQwMDcwMzE5ZDlmMDQyZjQ0ZGM0MmQzODdmZDM0OWU4In0.eyJhdWQiOiIxNTYwIiwianRpIjoiZDVmZTFkMjZlNTMxOWZlZWE1NmRkYWQzOTc3MWM5NDBlOTJmNGZmYWRlYmY2ZjBiNDAwNzAzMTlkOWYwNDJmNDRkYzQyZDM4N2ZkMzQ5ZTgiLCJpYXQiOjE2NzQwNTYyMDIsIm5iZiI6MTY3NDA1NjIwMiwiZXhwIjoxNjc0MTQyNjAyLCJzdWIiOiI1NTIyIiwic2NvcGVzIjpbXX0.Tz6oQcZt-5T71RCT4Z58ietT3-OAPzKBRWSAfADiI0ZAWfvuRyiS_7X9Ox_BcP71uT8ZAqcRGe7hKO8qzG7OLKi1VZCsOQh7ldDutC28cfVL8VxFp3bBytGNiqwYMFv6X7mJ--LdDO8TUi72F8PWguwzOcFVNY__l4N7ZcbgAmXv8rBUUgCZWBZtlYDYA0z95p4KbDMI1CfNjxF8PnH-WQHEzOHGBOAPts2EzgVUjrMx1xZ7J-HwQ9jKVBfGmbGLwtkgeeE6b55s9v7AcP611iKGNq2e7tn2CUEKezboJ_MEwfDok1a0g7C1rgphaahuLKvTA_bBVRMpW6WsbVlp4zzt-3P2lNeXx9rTA1aRxdSUnSHj_t-93lFf5PD7vpvnUSuGu5ZSv_A6WtCpsIdaO0H4PKJlb-Uj6o4ZincNd4_hkiDoylgXP1RbZhD2yJtBSvCrbidbWWytlezzKsQgHKOK64tppUELyWzS88uQ2Rgg9u4Quk19AnmtFai-0bOE4Cruf_aYwNwImdSbUXLCXuDisIA5mMTHaBlYbnCEQX5mkD5BNRDvnhw0iu0HPsbYulmVcwWh-FdwMjCDl8rsh9vHnyfL0DjqmA1GmB0YoEkSPvsT7dzYVqRuH3pd048t7pec-ZBipeV9SzaHePUd0qhMGY8oNNhAb-Z8oII6kY8";
    private static final String IDPAL_REFRESH_TOKEN = "def50200f66d8006819b72c3354863dd89fe28d84ad556933a23c8624cd59003c4150aba790ea3f3cd6c2e2422d4cb726bd81a70c0de6831c8ae8bbbec81d9b36344b87c2bf78ff40bb69df2f1d8377adba1e2cf96052c4fd715fd9ce8555cb86b1a1b3c6a95d1dfeb7f6a667c108c95535330da964890ecaa8952ae56f30fc0a561c1abdaa42de83f86f5f00ae97b258d688815044e270b7e19496bb03acdcfe0c863b2e5c85d1b21a4b4e61dcc9821d22f044af1c487446c34cd6785ac90c7b2c89e6eb6a895ac8dd1d321c9dd69a01e48659cae95f01b1fde29d2f5c716e5f88852e74b243d614ef8fcb0d7b74b51e208a1007aaca8eff6180ca3d6c22dad8162907e3f41abc3c1f812d53a957f98121bb273c49b0dd288428b743e8bee02c2271ccb589bfd664cb86c7cea1c75527efa5aac075a261cdc36b9ca2c26f3fa27e84bda34ca33ff302b9b99c34f449382efada175d43debad1161bc977d4fbbf42491536b4214";

    @BeforeEach
    void setUp() {
        when(idpalRequestMock.getClientKey()).thenReturn(CLIENT_KEY);
        when(idpalRequestMock.getAccessKey()).thenReturn(ACCESS_KEY);
        when(idpalRequestMock.getClientId()).thenReturn(CLIENT_ID);
        when(idpalRequestMock.getInformationType()).thenReturn(INFORMATION_TYPE);
        when(idpalRequestMock.getContact()).thenReturn(CONTACT);
        when(idpalRequestMock.getProfileId()).thenReturn(PROFILE_ID);
        when(idpalRequestMock.getRefreshToken()).thenReturn(IDPAL_REFRESH_TOKEN);
        when(idpalRequestMock.getSubmissionId()).thenReturn(SUBMISSION_ID);
        when(idpalRequestMock.getContentDisposition()).thenReturn(CONTENT_DISPOSITION);
        when(idpalWebhookRequestMock.getEventId()).thenReturn(WEBHOOK_EVENT_ID);
        when(idpalWebhookRequestMock.getEventType()).thenReturn(WEBHOOK_EVENT_TYPE);
        when(idpalWebhookRequestMock.getUuid()).thenReturn(WEBHOOK_UUID);
        when(idpalWebhookRequestMock.getSubmissionId()).thenReturn(WEBHOOK_SUBMISSION_ID);
        when(idpalWebhookRequestMock.getSource()).thenReturn(WEBHOOK_SOURCE);
        when(idpalAccessTokenMock.getAccessToken()).thenReturn(IDPAL_ACCESS_TOKEN);
        when(idpalAccessTokenMock.getRefreshToken()).thenReturn(IDPAL_REFRESH_TOKEN);
        when(idpalRenewAccessTokenMock.getClientKey()).thenReturn(CLIENT_KEY);
        when(idpalRenewAccessTokenMock.getAccessKey()).thenReturn(ACCESS_KEY);
        when(idpalRenewAccessTokenMock.getClientId()).thenReturn(CLIENT_ID);
        when(idpalRenewAccessTokenMock.getRefreshToken()).thenReturn(IDPAL_REFRESH_TOKEN);
    }

    @Test
    void testIdpalRequestDto() {
        assertEquals(CLIENT_KEY, idpalRequestMock.getClientKey());
        assertEquals(ACCESS_KEY, idpalRequestMock.getAccessKey());
        assertEquals(CLIENT_ID, idpalRequestMock.getClientId());
        assertEquals(INFORMATION_TYPE, idpalRequestMock.getInformationType());
        assertEquals(CONTACT, idpalRequestMock.getContact());
        assertEquals(PROFILE_ID, idpalRequestMock.getProfileId());
        assertEquals(IDPAL_REFRESH_TOKEN, idpalRequestMock.getRefreshToken());
        assertEquals(SUBMISSION_ID, idpalRequestMock.getSubmissionId());
        assertEquals(CONTENT_DISPOSITION, idpalRequestMock.getContentDisposition());
    }

    @Test
    void testIdpalWebhookRequestDto() {
        assertEquals(WEBHOOK_EVENT_ID, idpalWebhookRequestMock.getEventId());
        assertEquals(WEBHOOK_EVENT_TYPE, idpalWebhookRequestMock.getEventType());
        assertEquals(WEBHOOK_UUID, idpalWebhookRequestMock.getUuid());
        assertEquals(WEBHOOK_SUBMISSION_ID, idpalWebhookRequestMock.getSubmissionId());
        assertEquals(WEBHOOK_SOURCE, idpalWebhookRequestMock.getSource());
    }

    @Test
    void testIdpalAccessTokenDto() {
        assertEquals(IDPAL_ACCESS_TOKEN, idpalAccessTokenMock.getAccessToken());
        assertEquals(IDPAL_REFRESH_TOKEN, idpalAccessTokenMock.getRefreshToken());
    }

    @Test
    void testIdpalRenewAccessTokenDto() {
        assertEquals(CLIENT_KEY, idpalRenewAccessTokenMock.getClientKey());
        assertEquals(CLIENT_ID, idpalRenewAccessTokenMock.getClientId());
        assertEquals(ACCESS_KEY, idpalRenewAccessTokenMock.getAccessKey());
        assertEquals(IDPAL_REFRESH_TOKEN, idpalRenewAccessTokenMock.getRefreshToken());
    }
}
