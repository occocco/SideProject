package com.toy.overall_practice.redis;

import com.toy.overall_practice.domain.role.RoleType;
import com.toy.overall_practice.domain.member.Member;
import com.toy.overall_practice.jwt.JwtTokenProvider;
import com.toy.overall_practice.jwt.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
@SpringBootTest
@RequiredArgsConstructor
class RedisRepositoryTest {

    @Autowired
    RedisRepository redisRepository;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void init() {
        redisRepository.deleteAll();
    }
    @Test
    void saveTest(){

        Member member = Member.createMember("memberA", "1234", RoleType.MEMBER);
        Token token = jwtTokenProvider.createToken(member, 1000L);

        Token saveToken = redisRepository.save(token);

        assertThat(token.getKey()).isEqualTo(saveToken.getKey());
        assertThat(token.getValue()).isEqualTo(saveToken.getValue());
        assertThat(token.getExpiredTime()).isEqualTo(saveToken.getExpiredTime());

    }

    @Test
    void findByIdTest(){

        Member memberA = Member.createMember("memberA", "1234", RoleType.MEMBER);
        Token token = jwtTokenProvider.createToken(memberA, 1000L);
        redisRepository.save(token);

        Optional<Token> findToken = redisRepository.findById(memberA.getLoginId());

        if (findToken.isPresent()) {
            Token getToken = findToken.get();
            assertThat(getToken.getKey()).isEqualTo(memberA.getLoginId());
        }

    }

}