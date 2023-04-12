package com.toy.overall_practice.api.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.overall_practice.api.ResponseResult;
import com.toy.overall_practice.domain.post.Post;
import com.toy.overall_practice.service.post.PostService;
import com.toy.overall_practice.service.post.dto.PostCreateDto;
import com.toy.overall_practice.service.post.dto.PostDto;
import com.toy.overall_practice.service.post.dto.PostModifyDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Api(value = "거래글 REST API", tags = {"Post REST API"})
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostRestController {

    private final PostService postService;

    @ApiOperation(value = "거래글 등록")
    @PostMapping("/posts")
    public ResponseEntity<PostDto> savePost(@RequestBody PostCreateDto postCreateDto, Principal principal) {
        Post post = postService.savePost(postCreateDto, principal);
        PostDto postDto = PostDto.toPostDto(post);
        return ResponseEntity.ok().body(postDto);
    }

    @ApiOperation(value = "전체 거래글 조회")
    @GetMapping("/posts")
    public ResponseEntity<List<PostDto>> getPostList() {
        List<Post> posts = postService.findPosts();
        List<PostDto> postDtoList = posts.stream().map(PostDto::toPostDto).collect(Collectors.toList());
        return ResponseEntity.ok().body(postDtoList);
    }

    @ApiOperation(value = "거래글 상세 조회", notes = "path는 postId")
    @GetMapping("/posts/{id}")
    public ResponseEntity<PostDto> getPostDetail(@PathVariable @ApiParam(value = "거래글 번호") Long id) {
        Post post = postService.findPostById(id);
        PostDto postDto = PostDto.toPostDto(post);
        return ResponseEntity.ok().body(postDto);
    }

    @ApiOperation(value = "거래글 수정", notes = "path는 postId, RequsetBody PostModifyDto의 모든 필드 값은 필수")
    @PatchMapping(value = "/posts/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable @ApiParam(value = "거래글 번호") Long id,
                                              @RequestBody PostModifyDto postModifyDto) {
        Post post = postService.patchPost(id, postModifyDto);
        PostDto postDto = PostDto.toPostDto(post);
        return ResponseEntity.ok().body(postDto);
    }

    @ApiOperation(value = "거래글 상태 수정", notes = "path는 postId, RequsetBody는 '판매중', '판매완료', '판매취소'")
    @PatchMapping(value = "/posts/{id}/status")
    public ResponseEntity<PostDto> updatePostStatus(@PathVariable @ApiParam(value = "거래글 번호") Long id,
                                                    @RequestBody String status) {
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

    @ApiOperation(value = "거래글 삭제", notes = "path로 받은 postId를 통해 거래글 삭제")
    @DeleteMapping("/posts/{id}")
    public ResponseEntity<ResponseResult> deletePost(@PathVariable @ApiParam(value = "거래글 번호") Long id) {
        postService.removePost(id);
        return ResponseEntity.ok().body(new ResponseResult(HttpStatus.OK.value(), "거래글이 삭제 되었습니다."));
    }

}
