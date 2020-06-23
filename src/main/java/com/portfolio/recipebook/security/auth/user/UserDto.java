package com.portfolio.recipebook.security.auth.user;

import com.portfolio.recipebook.security.auth.user.group.AuthGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @NotNull(message = "Nickname must be not null and with at least 4 characters")
    @Size(min = 4, message = "Minimum characters for nickname is 4")
    private String nickname;
    @NotNull(message = "Password must be not null and with at least 6 characters")
    @Size(min = 6, message = "Minimum characters for password is 6")
    private String password;
    @NotNull(message = "Email must be not null")
    @Email(message = "Wrong email format")
    private String email;

    public User translateModelToUser() {
        return User.builder()
                .nickname(this.nickname)
                .password(new BCryptPasswordEncoder(11).encode(this.password))
                .email(this.email)
                .authGroup(AuthGroup.builder().id(1L).description("USER").build())
                .isActive(true)
                .build();
    }
}
