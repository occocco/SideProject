package com.toy.overall_practice.api.post;

import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.service.post.PostService;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import com.toy.overall_practice.service.post.dto.PostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

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

}
