package com.example.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class FCMNotificationSender {
	private static final String FCM_SERVER_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String FCM_SERVER_KEY = "AAAARlgwiA0:APA91bEMzZfCdGhmdAJG8tLRXANOFbHMPDFoFEfi_QV0wMGz6iPald0kqftXi0ONvQScRHFzEegRcf5JcxmreFehPIRXhafEEBYvDRvSdIaAt00XJHjTohpLF-CG8cqoxKA-HUS0l3_q";

    public void sendPushNotification(String deviceToken, String title, String body) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "key=" + FCM_SERVER_KEY);

        String payload = buildNotificationPayload(deviceToken, title, body);
        HttpEntity<String> entity = new HttpEntity<>(payload, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(FCM_SERVER_URL, entity, String.class);

        // response를 확인하고 필요에 따라 처리
        System.out.println("FCM Server Response: " + response.getBody());
    }

    private String buildNotificationPayload(String deviceToken, String title, String body) {
        return "{" +
                "\"to\":\"" + deviceToken + "\"," +
                "\"notification\":{" +
                "\"title\":\"" + title + "\"," +
                "\"body\":\"" + body + "\"" +
                "}" +
                "}";
    }

}
