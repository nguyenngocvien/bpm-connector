package com.bpm.core.mail.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class MailServerStatus {
    private boolean connected;
    private int successRate;
    private long avgSendTime;
}
