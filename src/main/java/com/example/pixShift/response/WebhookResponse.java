package com.example.pixShift.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebhookResponse {

    private String requestId;
    private String status;
    private String csvUrl;

}
