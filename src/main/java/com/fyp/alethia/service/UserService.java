package com.fyp.alethia.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fyp.alethia.dto.IDPalRequest;
import com.fyp.alethia.dto.UserRequest;
import lombok.SneakyThrows;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private static int PROFILE_ID = 2930;
    private static String INFORMATION_TYPE = "email";
    private static String CLIENT_KEY = "FD0BE5D1";
    private static String ACCESS_KEY = "FA71EB4C-5637-45F1-40A4-08B32F02E449";
    private static ObjectMapper MAPPER = new ObjectMapper();
    private final OkHttpClient httpClient = new OkHttpClient();
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @SneakyThrows
    public void createUser(UserRequest userRequest) {
        IDPalRequest idPalRequest = IDPalRequest.builder()
                .clientKey(CLIENT_KEY)
                .accessKey(ACCESS_KEY)
                .informationType(INFORMATION_TYPE)
                .contact(userRequest.getEmail())
                .profileId(PROFILE_ID)
                .build();

        RequestBody body = RequestBody.create(MAPPER.writeValueAsString(idPalRequest), JSON);

        Request request = new Request.Builder()
                .url("https://client.id-pal.com/3.0.0/api/app-link/send")
                .addHeader("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiIsImp0aSI6ImZkZjM2OGRkMmQzNzNjNmQ2MzIwZjUxNDJkYTc4ZDY2M2VkNWIzMTg0N2E1NzdiYjczNWY0Mzk0ZmE3N2FhNWFkZjE2ODliNTlmZmNlYzMzIn0.eyJhdWQiOiIxNTYwIiwianRpIjoiZmRmMzY4ZGQyZDM3M2M2ZDYzMjBmNTE0MmRhNzhkNjYzZWQ1YjMxODQ3YTU3N2JiNzM1ZjQzOTRmYTc3YWE1YWRmMTY4OWI1OWZmY2VjMzMiLCJpYXQiOjE2NjQ4MjIyMjIsIm5iZiI6MTY2NDgyMjIyMiwiZXhwIjoxNjY0OTA4NjIyLCJzdWIiOiI1NTIyIiwic2NvcGVzIjpbXX0.LRBdKhAw8q426l_mjAzOKoCWNE9qRBuo0FRapFQTdieIo2ScwkUWTN1Sa3tTajSK4FPzouqBptPN5zMlpzAmdmj5PFNIR6or-LE8bH6YBmIM4W2iH-bBN3PtZN77AiPyzEv08zTXmdv4KLaTmqyhiLAGOLYMTfvYImR-WPYiYa1laghQopMxpZNxej2PRZ01ZU_dyuS_LFkY7oj-M2hwZaZjxbMY04jbefm7w_J_bqLC1YEoJ678a4RCFs0WpeJNdAs7KI75bdFKGtCM4JTIJ1T5Hp1rgh1b6smxLXgK7bd9dh9-2wsWzyAXue59s32f9W7IPUHNnGX6vk89KoZQ3Vmllw7-Hm7VFEB2Q5YkzAtZJIwmG3ijLTM1vuKjhpW9U7yI1NkxM8rWcWLwrG3cFiISt1EWmS-hw6ZtX4uX9soZ672ujAKcWIPXgLE695j4Zou4AfCx1nw9Y4yVtTxSfgVtuOpTjG_6ATe335t05aMPGJBuMetCQWIB4WEdkHBlvvgz1UC3DkNWGYTeoKQiCI4l213n5_KbQ5qEqLi_xN0_G_221jDzHkCxjn4WqXxFEKPLTisWkQ-Csdit46d0cjD2wLB22G3i0VnfklTOX5oj0YnUh4Z1n6JLJ4GG4Z4J7VZraxquOaxtAoT65M1IjA9xZNTj6VoWwwlvEBNKpgA")
                .addHeader("Accept", "application/json")
                .post(body)
                .build();

        Response response = httpClient.newCall(request).execute();
        System.out.println(response.body().string());
    }
}
