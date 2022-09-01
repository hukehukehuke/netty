package com.huke.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.zalando.problem.spring.web.advice.ProblemHandling;

/**
 * @author huke
 * @date 2022/08/26/下午4:20
 */

@ControllerAdvice
public class ExceptionHandler implements ProblemHandling {
    @Override
    public boolean isCausalChainsEnabled() {
        return ProblemHandling.super.isCausalChainsEnabled();
    }
}
