package com.major.project.E_Wallet.App.Like.TransactionServiceApplication.config;

import com.major.project.E_Wallet.App.Like.TransactionServiceApplication.service.TxnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private TxnService txnService;

    @Autowired
    private CommonConfig commonConfig;

    @Value("${user.authority}")
    private String userAuthority;

    @Value("${admin.authority}")
    private String adminAuthority;

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(txnService);
        authProvider.setPasswordEncoder(commonConfig.passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        HttpSecurity httpSecurity = http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/txn/initTxn/**").hasAuthority(userAuthority)
                        .anyRequest().permitAll())
                .formLogin(Customizer.withDefaults()).httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());   //disabling csrf for now as we are working on postman
        return http.build();
    }
}
