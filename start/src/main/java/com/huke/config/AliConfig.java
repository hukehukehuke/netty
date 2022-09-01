package com.huke.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author huke
 * @date 2022/08/29/下午4:18
 */
@Configuration
@RequiredArgsConstructor
public class AliConfig {

    private final AppProperties appProperties;
    @Bean
    public IAcsClient iAcsClient(){
        DefaultProfile profile = DefaultProfile.getProfile(
                "cn-hangzhou",
                appProperties.getAli().getApiKey(),
                appProperties.getAli().getApiSecret()
        );
        return new DefaultAcsClient(profile);
    }
}
