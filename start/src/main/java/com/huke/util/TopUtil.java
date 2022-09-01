package com.huke.util;

import com.eatthepath.otp.TimeBasedOneTimePasswordGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

/**
 * @author huke
 * @date 2022/08/29/下午4:00
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class TopUtil {

    private static final long TIME_STEP = 60 * 5L;
    private static final int PASSWORD_LENGTH = 6;
    private KeyGenerator keyGenerator;

    private TimeBasedOneTimePasswordGenerator totp;

    {
        try {
            totp = new TimeBasedOneTimePasswordGenerator(Duration.ofSeconds(TIME_STEP), PASSWORD_LENGTH);
            keyGenerator = KeyGenerator.getInstance(totp.getAlgorithm());
            keyGenerator.init(512);
        } catch (Exception e) {
            log.error("没有找到算法{}", e.getLocalizedMessage());
        }

    }

    public String createTotp(Key key, Instant time) throws InvalidKeyException {
        val password = totp.generateOneTimePassword(key, time);
        val fomate = "%0" + PASSWORD_LENGTH + "d";
        return String.format(fomate, password);
    }

    public Optional<String> createTotp(String strKey) {
        try {
            return Optional.ofNullable(createTotp(decodeKeyFromString(strKey), Instant.now()));
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }


    public boolean verifyTotp(Key key, String code) throws InvalidKeyException {
        val now = Instant.now();
        return code.equals(createTotp(key, now));
    }

    public Key generateKey() {
        return keyGenerator.generateKey();
    }

    public String encodeKeyToString(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public String encodeKeyToString() {
        return encodeKeyToString(generateKey());
    }

    public Key decodeKeyFromString(String strKey) {
        return new SecretKeySpec(Base64.getDecoder().decode(strKey), totp.getAlgorithm());
    }
}
