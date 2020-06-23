package com.portfolio.recipebook.security.auth.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.portfolio.recipebook.security.auth.user.group.AuthGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user", schema = "test")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @Column(name = "id_user")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;
    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "id_user_type", nullable = false, referencedColumnName = "id_user_type")
    private AuthGroup authGroup;
}
