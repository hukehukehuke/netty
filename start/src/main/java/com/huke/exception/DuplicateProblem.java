package com.huke.exception;

import com.huke.config.Constans;
import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

import java.net.URI;

/**
 * @author huke
 * @date 2022/08/29/下午3:35
 */
public class DuplicateProblem extends AbstractThrowableProblem {

    private static final URI TYPE = URI.create(Constans.PROBLEM_BASE_URI + "/");

    public DuplicateProblem(String message) {
        super(TYPE,"发现重复数据",Status.CONFLICT,message);
    }
}
