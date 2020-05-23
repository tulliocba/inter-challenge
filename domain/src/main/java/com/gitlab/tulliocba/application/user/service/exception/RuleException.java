package com.gitlab.tulliocba.application.user.service.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RuleException extends RuntimeException {
    public RuleException(String msg) {
        super(msg);
        log.warn(msg);
    }
}
