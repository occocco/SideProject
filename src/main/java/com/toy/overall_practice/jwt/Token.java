package com.toy.overall_practice.jwt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash("auth")
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @Indexed
    private String key;

    private String value;

    @TimeToLive
    private Long expiredTime;

}
