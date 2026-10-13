package com.tarakki.support.client;

import com.tarakki.support.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberClient {

    private final RestClient memberServiceRestClient;

    @Value("${member.api.endpoint}")
    private String memberApiEndpoint;

    public void validateMemberExists(UUID memberId) {
        try {
            memberServiceRestClient.get()
                    .uri(memberApiEndpoint + "/{memberId}", memberId)
                    .retrieve()
                    .toBodilessEntity();

        } catch (HttpClientErrorException.NotFound exception) {
            throw new MemberNotFoundException(memberId);
        }
    }
}