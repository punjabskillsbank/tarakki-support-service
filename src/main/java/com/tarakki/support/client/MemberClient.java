package com.tarakki.support.client;

import com.tarakki.common.dto.MemberDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MemberClient {

    private final RestClient memberServiceRestClient;

    public MemberDTO getMemberById(UUID memberId) {
        return memberServiceRestClient.get()
                .uri("/{memberId}", memberId)
                .retrieve()
                .body(MemberDTO.class);
    }
}