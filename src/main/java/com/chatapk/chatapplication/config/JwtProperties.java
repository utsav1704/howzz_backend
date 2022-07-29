package com.chatapk.chatapplication.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

@ConfigurationProperties(prefix = "jwt")
@Data
@Service
public class JwtProperties {

    private String secretKey = "chat";

    // validity in milliseconds
    private long validityInMs = 1000 * 60 * 60 * 24; // 1 day

}
