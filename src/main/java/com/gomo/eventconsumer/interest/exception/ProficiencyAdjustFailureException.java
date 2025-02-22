package com.gomo.eventconsumer.interest.exception;

import com.gomo.eventconsumer.common.exception.DomainException;

public class ProficiencyAdjustFailureException extends DomainException {

	public ProficiencyAdjustFailureException(InterestErrorCode errorCode) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
	}

	public ProficiencyAdjustFailureException(InterestErrorCode errorCode, Throwable cause) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage(), cause);
	}
}
