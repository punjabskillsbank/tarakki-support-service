package com.tarakki.support.dto;

import com.tarakki.support.entity.IssueCategory;
import com.tarakki.support.entity.IssueType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicketRequestDTO {

    @NotNull
    private UUID memberId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String subject;

    @NotNull
    private IssueCategory issueCategory;

    @NotNull
    private IssueType issueType;

    private String message;
}