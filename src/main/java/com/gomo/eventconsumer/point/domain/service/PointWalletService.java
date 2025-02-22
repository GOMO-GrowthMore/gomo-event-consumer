package com.gomo.eventconsumer.point.domain.service;

import static com.gomo.eventconsumer.common.exception.DomainErrorCode.*;

import org.springframework.transaction.annotation.Transactional;

import com.gomo.eventconsumer.common.domain.service.DomainService;
import com.gomo.eventconsumer.common.exception.NotFoundException;
import com.gomo.eventconsumer.point.domain.model.Balance;
import com.gomo.eventconsumer.point.domain.model.PointWallet;
import com.gomo.eventconsumer.point.domain.model.TransactionType;
import com.gomo.eventconsumer.point.domain.model.TransactorId;
import com.gomo.eventconsumer.point.domain.repository.PointWalletRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class PointWalletService {

	private final PointWalletRepository pointWalletRepository;

	@Transactional
	public void adjustPointBalance(TransactorId transactorId, TransactionType transactionType, int deltaAmount) {
		PointWallet pointWallet = findPointWalletByTransactorId(transactorId);
		pointWallet.adjustBalance(transactionType.getOperationType() * deltaAmount);
	}

	public Balance findBalance(TransactorId transactorId) {
		PointWallet pointWallet = findPointWalletByTransactorId(transactorId);
		return pointWallet.getBalance();
	}

	private PointWallet findPointWalletByTransactorId(TransactorId transactorId) {
		return pointWalletRepository.findByTransactorId(transactorId)
			.orElseThrow(() -> new NotFoundException(NOT_FOUND, "PointWallet not found with transactor id: " + transactorId.getId()));
	}
}
