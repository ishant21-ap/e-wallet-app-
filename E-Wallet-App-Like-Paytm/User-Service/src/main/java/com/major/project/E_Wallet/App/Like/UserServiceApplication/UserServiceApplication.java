package com.major.project.E_Wallet.App.Like.UserServiceApplication;

import com.major.project.E_Wallet.App.Like.UserServiceApplication.model.UserType;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.model.Users;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class UserServiceApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${service.Authority}")
	private String serviceAuthority;

	@Autowired
	private UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}


	//This we are using to authenticate the txn service
	//Using this we are making an handshake between txn service and user service, whenever this txn service make
	//connection to user service they need to pass username (contact) and password
	@Override
	public void run(String... args) throws Exception {
		Users users = Users.builder()
				.contact("txn-service")
				.password(passwordEncoder.encode("txn-service"))
				.enabled(true)
				.accountNonExpired(true)
				.accountNonLocked(true)
				.credentialsExpired(true)
				.email("txnService@gmail.com").authorities(serviceAuthority)
				.userType(UserType.SERVICE).build();
		userRepository.save(users);
	}
}
