package com.huke.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.IAcsClient;
import com.huke.config.AppProperties;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

/**
 * @author huke
 * @date 2022/08/29/下午4:17
 */
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "", name = ",", havingValue = "")
public class SmsServiceImpl implements SmsService {

    private final IAcsClient iAcsClient;
    private final AppProperties appProperties;

    @Override
    public void send(String mobile, String msg) {
        val request = new CommonRequest();
        request.setAction("");
        request.setDomain("");
    }
}
