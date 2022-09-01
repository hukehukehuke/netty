package com.huke.util;

import java.util.Optional;

/**
 * @author huke
 * @date 2022/08/29/下午4:50
 */
public class TotpUtil {

    private static long TIME_STEP = 0L;
    public long getTimeStepInSeconds(){
        return TIME_STEP;
    }

    public Optional<String> creatTotp(String key) {
        return null;
    }
}
