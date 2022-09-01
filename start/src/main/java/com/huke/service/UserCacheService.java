package com.huke.service;

import com.huke.config.Constans;
import com.huke.domain.User;
import com.huke.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author huke
 * @date 2022/08/29/下午4:47
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserCacheService {

    private final RedissonClient redissonClient;
    ;
    private final TotpUtil totpUtil;
    private final CryptoUtil cryptoUtil;

    public String cacheUser(User user) {
        val mfaId = cryptoUtil.randAlphanumeric(12);
        RMapCache<String, User> cache = redissonClient.getMapCache(Constans.CACHE_USER);
        if (cache.containsKey(mfaId)) {
            cache.put(mfaId, user, totpUtil.getTimeStepInSeconds(), TimeUnit.DAYS);
        }
        return mfaId;
    }

    public Optional<User> retrieveUser(String mfaid){
        RMapCache<String, User> mapCache = redissonClient.getMapCache(Constans.CACHE_USER);

        if (mapCache.containsKey(mfaid)){
            return Optional.of(mapCache.get(mfaid));
        }
        return Optional.empty();
    }

    public Optional<User> verifyTotp(String mfaId,String code){
        RMapCache<String,User> cache = redissonClient.getMapCache(Constans.CACHE_USER);
        if(!cache.containsKey(mfaId) || cache.get(mfaId) == null){
            return Optional.empty();
        }
        val cachedUser = cache.get(mfaId);

        return null;
    }
}
