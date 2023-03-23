package com.toy.overall_practice.api.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.service.post.PostService;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import com.toy.overall_practice.service.post.dto.PostDto;
import com.toy.overall_practice.service.post.dto.PostModifyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @PostMapping("/posts")
    public ResponseEntity<PostDto> savePost(@RequestBody PostCreateDto postCreateDto, Principal principal) {
        Post post = postService.savePost(postCreateDto, principal);
        PostDto postDto = PostDto.toPostDto(post);
        return ResponseEntity.ok().body(postDto);
    }

    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPosts() {
        List<Post> posts = postService.findPosts();
        List<PostDto> postDtoList = posts.stream().map(PostDto::toPostDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(postDtoList);
    }

    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long id) {
        Post post = postService.findPostById(id);
        PostDto postDto = PostDto.toPostDto(post);
        return ResponseEntity.ok().body(postDto);
    }

    @PatchMapping(value = "/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostModifyDto postModifyDto) {
        Post post = postService.patchPost(id, postModifyDto);
        PostDto postDto = PostDto.toPostDto(post);
        return ResponseEntity.ok().body(postDto);
    }

    @PatchMapping(value = "/posts/{id}/status")
    public ResponseEntity<PostDto> updatePostStatus(@PathVariable Long id, @RequestBody String status) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String category = mapper.readTree(status).get("status").asText();
            Post post = postService.modifyPostStatus(id, category);
            PostDto postDto = PostDto.toPostDto(post);
            return ResponseEntity.ok().body(postDto);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("잘못된 요청입니다.", e);
        }
    }

}
