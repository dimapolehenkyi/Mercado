package com.example.mercado.common.repositories;

import com.example.mercado.common.jwt.entity.Token;
import com.example.mercado.common.jwt.repositories.TokenRepository;
import com.example.mercado.users.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
public class TokenRepositoryTest {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @DisplayName("findAllAccessTokenByUser_Id should find all access token by user_id")
    void findAllAccessTokenByUser_Id_shouldFindAllAccessTokenByUser_Id() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("email@gmail.com");

        entityManager.persist(user);

        Token token1 = new Token();
        token1.setAccessToken("access1");
        token1.setRefreshToken("refresh1");
        token1.setUser(user);

        Token token2 = new Token();
        token2.setAccessToken("access2");
        token2.setRefreshToken("refresh2");
        token2.setUser(user);

        entityManager.persist(token1);
        entityManager.persist(token2);
        entityManager.flush();

        List<Token> tokens = tokenRepository.findAllAccessTokenByUser_Id(user.getId());

        Assertions.assertEquals(2, tokens.size());

        List<String> tokenValues = tokens.stream()
                .map(Token::getAccessToken)
                .toList();

        Assertions.assertTrue(tokenValues.contains("access1"));
        Assertions.assertTrue(tokenValues.contains("access2"));
    }

    @Test
    @DisplayName("findByAccessToken should return token when exists")
    void findByAccessToken_shouldReturnToken_whenExist() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("test@gmail.com");
        entityManager.persist(user);

        Token token = new Token();
        token.setAccessToken("access123");
        token.setRefreshToken("refresh123");
        token.setUser(user);

        entityManager.persist(token);
        entityManager.flush();

        Optional<Token> found = tokenRepository.findByAccessToken("access123");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("access123", found.get().getAccessToken());
    }


    @Test
    @DisplayName("findByAccessToken should return empty when token not exists")
    void findByAccessToken_shouldReturnEmpty_whenNotExist() {
        Optional<Token> found = tokenRepository.findByAccessToken("not_existing");
        Assertions.assertTrue(found.isEmpty());
    }




    @Test
    @DisplayName("findByRefreshToken should return token when exists")
    void findByRefreshToken_shouldReturnToken_whenExist() {
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setEmail("test@gmail.com");
        entityManager.persist(user);

        Token token = new Token();
        token.setAccessToken("access123");
        token.setRefreshToken("refresh123");
        token.setUser(user);

        entityManager.persist(token);
        entityManager.flush();

        Optional<Token> found = tokenRepository.findByRefreshToken("refresh123");
        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("refresh123", found.get().getRefreshToken());
    }


    @Test
    @DisplayName("findByRefreshToken should return empty when token not exists")
    void findByRefreshToken_shouldReturnEmpty_whenNotExist() {
        Optional<Token> found = tokenRepository.findByRefreshToken("not_existing");
        Assertions.assertTrue(found.isEmpty());
    }

}
