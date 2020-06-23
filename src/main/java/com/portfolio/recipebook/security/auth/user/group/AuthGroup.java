package com.portfolio.recipebook.security.auth.user.group;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_type", schema = "test")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthGroup {
    @Id
    @Column(name = "id_user_type")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;
    @Column(name = "description")
    private String description;
}
