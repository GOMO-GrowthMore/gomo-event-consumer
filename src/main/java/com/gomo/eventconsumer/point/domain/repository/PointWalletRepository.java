package com.gomo.eventconsumer.point.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gomo.eventconsumer.point.domain.model.PointWallet;
import com.gomo.eventconsumer.point.domain.model.PointWalletId;
import com.gomo.eventconsumer.point.domain.model.TransactorId;

public interface PointWalletRepository extends JpaRepository<PointWallet, PointWalletId> {

	Optional<PointWallet> findByTransactorId(TransactorId transactorId);
}
