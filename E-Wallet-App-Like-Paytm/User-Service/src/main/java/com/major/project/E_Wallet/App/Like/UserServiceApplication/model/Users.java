package com.major.project.E_Wallet.App.Like.UserServiceApplication.model;

import com.major.project.E_Wallet.App.Like.Utilities.UserIdentifier;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

@Entity
@ToString
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;

    @Column(unique = true, nullable = false)
    private String contact;

    @Column(unique = true, nullable = false)
    private String email;

    private String authorities;

    private String password;

    private boolean enabled;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsExpired;

    private String name;

    private String address;

    private String dob;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    //Whenever we are making an new wallet we are taking a unique identifier of user
    @Enumerated
    private UserIdentifier userIdentifier;

    private String userIdentifierValue;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(authorities.split(",")).map(a -> new SimpleGrantedAuthority(a)).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return contact;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
