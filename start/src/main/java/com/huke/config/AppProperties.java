package com.huke.config;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author huke
 * @date 2022/08/29/下午2:23
 */
@Configuration
@ConfigurationProperties(prefix = "mooc")
public class AppProperties {

    @Setter
    @Getter
    private Jwt jwt = new Jwt();


    @Setter
    @Getter
    private Ali ali = new Ali();

    @Setter
    @Getter
    public static class Ali {

        private String apiKey;

        private String apiSecret;

    }

    @Setter
    @Getter
    public static class Jwt {
        private String header = "Authorization";
        private String prefix = "prefix";

        private Long accessTokenExpireTime = 60_000L;

        private Long refreshTokenExpireTime = 30 * 3600 * 60L;

    }
}
