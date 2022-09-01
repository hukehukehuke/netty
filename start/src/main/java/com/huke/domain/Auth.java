package com.huke.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author huke
 * @date 2022/08/29/下午2:59
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Auth implements Serializable {
    private String accessToken;
    private String refreshToken;
}
