package com.huke.config;

import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @author huke
 * @date 2022/08/29/下午4:32
 */
@Configuration
public class LeanCloudConfig {

    @PostConstruct
    public void initialize(){
       // AVOSCloud.initialize();
    }
}
