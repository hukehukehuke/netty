package com.huke.service;

import com.huke.config.Constans;
import com.huke.domain.Auth;
import com.huke.domain.User;
import com.huke.repository.RoleRepo;
import com.huke.repository.UserRepo;
import com.huke.util.JwtUtil;
import com.huke.util.TotpUtil;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.Optional;
import java.util.Set;

/**
 * @author huke
 * @date 2022/08/29/下午3:02
 */
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    private final RoleRepo roleRepo;

    private final TotpUtil totpUtil;

    @Transactional(rollbackOn = Exception.class)
    public void register(User user) {
        roleRepo.findOptionalByAuthority(Constans.ROLE_USER)
                .map(role -> {
                    val userToSave = user.withAuthorities(Set.of(role))
                            .withPassword(passwordEncoder.encode(user.getPassword()));
                    return userRepo.save(userToSave);
                })
                .orElseThrow();
    }

    public Auth login(String username, String password) {
        return userRepo.findOptionalByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()))
                .map(user -> new Auth(
                        jwtUtil.createAccessToken(user),
                        jwtUtil.createRefreshToken(user)
                ))
                .orElseThrow(() -> new BadCredentialsException("用户名或密码错误"));
    }

    public boolean isUsernameExisted(String username) {
        return userRepo.countByUsername(username) > 0;
    }

    public boolean isEmailExisted(String email) {
        return userRepo.countByEmail(email) > 0;
    }

    public boolean isMobileExisted(String mobile) {
        return userRepo.countByMobile(mobile) > 0;
    }

    public Optional<User> findOptionByUsernameAndPassword(String username, String password) {
        return userRepo.findOptionalByUsername(username);
    }

    public UserDetails updatePassword(User user, String newPassword) {
        return userRepo.findOptionalByUsername(user.getUsername())
                .map(u -> userRepo.save(u.withPassword(newPassword)))
                .orElse(user);
    }

    public Optional<String> createTotp(String key){
        return totpUtil.creatTotp(key);
    }
}
