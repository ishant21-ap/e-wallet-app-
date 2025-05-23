package com.major.project.E_Wallet.App.Like.TransactionServiceApplication.Repository;

import com.major.project.E_Wallet.App.Like.TransactionServiceApplication.model.Txn;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TxnRepo extends JpaRepository<Txn, Integer> {
}
