package com.toy.overall_practice.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@RedisHash("auth")
@ToString
@AllArgsConstructor
public class JwtToken {

    @Id
    @Indexed
    private String key;

    private String value;

    @TimeToLive
    private Long expiredTime;

}
