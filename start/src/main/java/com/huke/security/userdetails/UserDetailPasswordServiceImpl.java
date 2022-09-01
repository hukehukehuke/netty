package com.huke.security.userdetails;


import com.huke.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.stereotype.Service;

/**
 * @author huke
 * @date 2022/08/29/上午11:41
 */
@Service
@RequiredArgsConstructor
public class UserDetailPasswordServiceImpl implements UserDetailsPasswordService {

    private final UserRepo userRepo;


    @Override
    public UserDetails updatePassword(UserDetails userDetails, String newPassword) {
        return userRepo.findOptionalByUsername(userDetails.getUsername())
                .map(user -> {
                    return (UserDetails)userRepo.save(user.withPassword(newPassword));
                })
                .orElse(userDetails);
    }
}
