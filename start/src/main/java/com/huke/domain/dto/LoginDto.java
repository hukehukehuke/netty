package com.huke.domain.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author huke
 * @date 2022/08/29/下午3:00
 */
@AllArgsConstructor
@NonNull
@NoArgsConstructor
@Data
@Builder
public class LoginDto implements Serializable {

    private String username;

    private String password;
}
