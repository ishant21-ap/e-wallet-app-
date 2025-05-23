package com.major.project.E_Wallet.App.Like.WalletServiceApplication.repository;

import com.major.project.E_Wallet.App.Like.WalletServiceApplication.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface WalletRepo extends JpaRepository<Wallet, Long> {
    Wallet findByContact(String contact);

    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance = w.balance + :amount where w.contact = :contact")
    void updateWallet(String receiver, double v);
}
