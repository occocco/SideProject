package com.toy.overall_practice.domain.post.repository;

import com.toy.overall_practice.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
