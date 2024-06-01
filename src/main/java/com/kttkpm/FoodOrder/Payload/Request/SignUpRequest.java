package com.kttkpm.FoodOrder.Payload.Request;

import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;
import java.util.Set;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpRequest {
    private int id;
    @NotNull
    @NotBlank(message = "User name khong duoc bo trong")
    private String username;

    @NotNull
    @NotBlank(message = "Email khong duoc bo trong")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$")
    private String email;

    @NotNull
    @NotBlank(message = "Password khong duoc bo trong")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}" )
    private String password;
    @NotBlank
    private String rePassword;
    private int idRole;
    private Date createDate;
    private String phone;
    private String address;

    @PrePersist
    protected void onCreate() {
        createDate = new Date();
    }


}