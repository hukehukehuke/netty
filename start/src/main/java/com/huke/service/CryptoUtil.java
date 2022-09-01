package com.huke.service;

import lombok.val;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * @author huke
 * @date 2022/08/29/下午4:54
 */
@Component
public class CryptoUtil {

    public  String randAlphanumeric(int targetStringLength){
        int leftLimit = 48;
        int rightLimit = 122;
        val random = new Random();
        return random.ints(leftLimit,rightLimit+1)
                .filter(i -> (i <= 57 || i>= 65))
                .limit(targetStringLength)
                .collect(StringBuilder::new,StringBuilder::appendCodePoint,StringBuilder::append)
                .toString();
    }
}
