package com.huke.rest;

import com.huke.domain.Auth;
import com.huke.domain.User;
import com.huke.domain.dto.LoginDto;
import com.huke.domain.dto.SendTotpDto;
import com.huke.domain.dto.UserDto;
import com.huke.exception.DuplicateProblem;
import com.huke.service.SmsService;
import com.huke.service.UserCacheService;
import com.huke.service.UserService;
import com.huke.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

/**
 * @author huke
 * @date 2022/08/26/下午3:40
 */
@RequestMapping("/authorize")
@RestController
@RequiredArgsConstructor
public class AuthorizeResource {


    private final UserService userService;

    private final UserCacheService userCacheService;

    private final JwtUtil jwtUtil;

    private final SmsService smsService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserDto userDto) {
        if (userService.isEmailExisted(userDto.getUsername())) {
            throw new DuplicateProblem("用户名重复");
        }
        if (userService.isEmailExisted(userDto.getEmail())) {
            throw new DuplicateProblem("电子邮箱重复");
        }

        val user = User.builder().build();

        return userDto;
    }

    @PostMapping("token")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) throws Exception {
        return userService.findOptionByUsernameAndPassword(loginDto.getUsername(), loginDto.getPassword())
                .map(user -> {
                    userService.updatePassword(user, loginDto.getPassword());

                    if (!user.isUsingMfa()) {
                        //return ResponseEntity.ok().build(userService.login(loginDto.getUsername(),loginDto.getPassword()));
                    }
                    val mfaId = userCacheService.cacheUser(user);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                            .header("x-", "", "")
                            .build();
                })

                .orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));

    }


    @PutMapping("totp")
    public void sendTotp(@Valid @RequestBody SendTotpDto sendTotpDto) {
        userCacheService.retrieveUser(sendTotpDto.getMfaId())
                .flatMap(user -> userService.createTotp(user.getMfaKey()).map(totp -> Pair.of(user, totp)))
                .ifPresentOrElse(pair -> {
                    if (Objects.equals(sendTotpDto.getMfType(), "MSS")) {
                        smsService.send(pair.getFirst().getEmail(), pair.getSecond());
                    }
                    if (Objects.equals(sendTotpDto.getMfType(), "Email")) {
                        smsService.send(pair.getFirst().getName(), pair.getSecond());
                    }
                }, () -> {
                    throw new RuntimeException();
                });

    }

    @PostMapping("/token/refresh")
    public Auth refreshToken(@RequestHeader(name = "Authorization") String authorization,
                             @RequestParam String refreshToken) throws AccessDeniedException {
        val PREFIX = "Bearer";
        val accessToken = authorization.replace(PREFIX, "");
        if (jwtUtil.validateRefreshToken(accessToken) &&
                jwtUtil.validateAccessToken(refreshToken) &&
                jwtUtil.validateAccessTokenWithOutExpireTime(toString())) {
            return new Auth(jwtUtil.createAccessTokenRefreshToken(refreshToken), refreshToken);
        }
        throw new AccessDeniedException("访问被拒绝");
    }
}
