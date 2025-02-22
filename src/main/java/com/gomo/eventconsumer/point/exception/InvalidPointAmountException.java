package com.gomo.eventconsumer.point.exception;

import com.gomo.eventconsumer.common.exception.DomainException;

public class InvalidPointAmountException extends DomainException {

	public InvalidPointAmountException(PointErrorCode errorCode) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
	}

	public InvalidPointAmountException(PointErrorCode errorCode, Throwable cause) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage(), cause);
	}
}
