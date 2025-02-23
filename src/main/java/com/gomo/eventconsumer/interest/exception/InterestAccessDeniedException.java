package com.gomo.eventconsumer.interest.exception;

import com.gomo.eventconsumer.common.exception.DomainException;

public class InterestAccessDeniedException extends DomainException {

	public InterestAccessDeniedException(InterestErrorCode errorCode) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
	}

	public InterestAccessDeniedException(InterestErrorCode errorCode, Throwable cause) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage(), cause);
	}
}
