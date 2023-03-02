package com.toy.overall_practice.redis;

import com.toy.overall_practice.jwt.JwtToken;
import org.springframework.data.repository.CrudRepository;

public interface RedisRepository extends CrudRepository<JwtToken,String> {
}
