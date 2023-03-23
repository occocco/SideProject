package com.toy.overall_practice.domain.post.repository;

import com.toy.overall_practice.domain.post.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"goods"})
    List<Post> findAll();
    @EntityGraph(attributePaths = {"goods"})
    @Query("SELECT p FROM Post p WHERE p.status = 'SELLING'")
    List<Post> findAllSellingPosts();
}
