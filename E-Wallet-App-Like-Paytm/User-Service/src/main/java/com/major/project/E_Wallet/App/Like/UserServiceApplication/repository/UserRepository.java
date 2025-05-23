package com.major.project.E_Wallet.App.Like.UserServiceApplication.repository;

import com.major.project.E_Wallet.App.Like.UserServiceApplication.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Integer> {

    Users findByContact(String contact);
}
