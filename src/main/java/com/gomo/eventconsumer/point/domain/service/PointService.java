package com.gomo.eventconsumer.point.domain.service;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

import com.gomo.eventconsumer.common.domain.service.DomainService;
import com.gomo.eventconsumer.common.util.UUIDGenerator;
import com.gomo.eventconsumer.point.domain.model.Point;
import com.gomo.eventconsumer.point.domain.model.PointId;
import com.gomo.eventconsumer.point.domain.model.SourceType;
import com.gomo.eventconsumer.point.domain.model.TransactionType;
import com.gomo.eventconsumer.point.domain.model.TransactorId;
import com.gomo.eventconsumer.point.domain.repository.PointRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@DomainService
public class PointService {

	private final PointWalletService pointWalletService;
	private final PointRepository pointRepository;

	@Transactional
	public void create(TransactorId transactorId, SourceType sourceType, TransactionType transactionType, int amount) {
		Point point = Point.of(
			PointId.of(UUIDGenerator.generate()),
			transactorId,
			sourceType,
			transactionType,
			amount,
			sourceType.getDescription() + transactionType.getDescription(),
			LocalDateTime.now()
		);

		pointWalletService.adjustPointBalance(transactorId, transactionType, amount);
		pointRepository.save(point);
	}
}
