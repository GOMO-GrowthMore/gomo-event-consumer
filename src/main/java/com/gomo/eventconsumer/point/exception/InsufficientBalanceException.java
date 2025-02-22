package com.gomo.eventconsumer.point.exception;

import com.gomo.eventconsumer.common.exception.DomainException;

public class InsufficientBalanceException extends DomainException {

	public InsufficientBalanceException(PointWalletErrorCode errorCode) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
	}

	public InsufficientBalanceException(PointWalletErrorCode errorCode, Throwable cause) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage(), cause);
	}
}
