package com.fresh.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "external")
@Getter
@Setter
@ToString
public class ThirdApiProperties {
    private String baseUrl;
    private String appId;
    private String secretKey;
}
