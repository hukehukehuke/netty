package com.huke.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

/**
 * @author huke
 * @date 2022/08/29/下午5:16
 */

@Data
@AllArgsConstructor
@NonNull
@NoArgsConstructor
public class SendTotpDto implements Serializable {
    private String mfaId;
    private String mfType;
}
