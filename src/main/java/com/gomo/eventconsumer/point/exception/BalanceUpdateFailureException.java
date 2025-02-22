package com.gomo.eventconsumer.point.exception;

import com.gomo.eventconsumer.common.exception.DomainException;

public class BalanceUpdateFailureException extends DomainException {

	public BalanceUpdateFailureException(PointWalletErrorCode errorCode) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage());
	}

	public BalanceUpdateFailureException(PointWalletErrorCode errorCode, Throwable cause) {
		super(errorCode.getHttpStatus(), errorCode.name(), errorCode.getMessage(), cause);
	}
}
