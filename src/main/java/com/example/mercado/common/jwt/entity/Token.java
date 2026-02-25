package com.example.mercado.common.jwt.entity;

import com.example.mercado.users.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Table(name = "token_table")
public class Token {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "access_token",  nullable = false,  unique = true,  length = 1000)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false, unique = true, length = 1000)
    private String refreshToken;

    @Column(name = "is_logged_out", nullable = false)
    private boolean loggedOut;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
