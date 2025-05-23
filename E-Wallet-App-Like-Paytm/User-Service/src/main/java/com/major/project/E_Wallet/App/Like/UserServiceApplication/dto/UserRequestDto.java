package com.major.project.E_Wallet.App.Like.UserServiceApplication.dto;

import com.major.project.E_Wallet.App.Like.Utilities.UserIdentifier;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.model.UserType;
import com.major.project.E_Wallet.App.Like.UserServiceApplication.model.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Email can not be blank.")
    private String email;
    private String name;

    @NotBlank(message = "Contact can not be blank.")
    private String contact;
    private String address;
    private String dob;

    @NotBlank(message = "password can not be blank.")
    private String password;

    @NotNull(message = "userIdentifier can not be blank.")
    private UserIdentifier userIdentifier;

    @NotBlank(message = "userIdentifierValue can not be blank.")
    private String userIdentifierValue;

    public Users toUser() {
        return Users.builder()
                .name(this.name)
                .contact(this.contact)
                .email(this.email)
                .address(this.address)
                .dob(this.dob)
                .userIdentifier(this.userIdentifier)
                .userIdentifierValue(this.userIdentifierValue)
                .password(this.password)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsExpired(true)
                .userType(UserType.USER)
                .build();
    }
}
