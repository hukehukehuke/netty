package com.huke.domain.dto;

import com.huke.annotation.ValidEmail;
import com.huke.annotation.ValidPassword;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author huke
 * @date 2022/08/26/下午3:37
 */
@Data
public class UserDto implements Serializable {
    @NonNull
    @NotBlank
    @Size(min = 4,max = 50,message = "用户名长度必须在4-50个字符之间")
    private String username;

    @NonNull
    @NotBlank
    @Size(min = 4,max = 50,message = "用户名长度必须在4-50个字符之间")
    @ValidPassword
    private String password;

    @NonNull
    @NotBlank
    @ValidPassword
    @Size(min = 4,max = 50,message = "用户名长度必须在4-50个字符之间")
    private String matchPassword;

    @NonNull
    @Email
    @ValidEmail
    private String email;

    private String name;
}
