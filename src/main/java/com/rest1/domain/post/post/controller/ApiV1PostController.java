package com.rest1.domain.post.post.controller;

import com.rest1.domain.post.post.dto.PostDto;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.service.PostService;
import com.rest1.global.rsData.RsData;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class ApiV1PostController {

    private final PostService postService;

    @GetMapping
    @Transactional(readOnly = true)
    public List<PostDto> getItems() {
        return postService.findAll().stream()
                .map(PostDto::new)
                .toList();
    }

    @GetMapping("/{id}")
    @Transactional(readOnly = true)
    public PostDto getItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        return new PostDto(post);
    }

    @DeleteMapping("/{id}")
    public RsData<Void> deleteItem(
            @PathVariable Long id
    ) {
        Post post = postService.findById(id).get();
        postService.delete(post);

        return new RsData<Void>(
                "204-1",
                "%d번 게시물이 삭제되었습니다.".formatted(id)
        );
    }

    record PostWriteReqBody(
            @NotBlank
            @Size(min = 2, max = 10)
            String title,
            @NotBlank
            @Size(min = 2, max = 100)
            String content
    ) {

    }

    record PostWriteResBody(
            PostDto postDto,
            Long totalCount
    ) {

    }

    @PostMapping
    public ResponseEntity<RsData<PostWriteResBody>> createItem(
            @RequestBody @Valid PostWriteReqBody reqBody
    ) {
        Post post = postService.write(reqBody.title, reqBody.content);

        long totalCount=postService.count();

        // 1. RsData 객체를 먼저 온전하게 만듭니다.
        RsData<PostWriteResBody> rsData = new RsData<>(
                "201-1",
                "%d번 게시물이 생성되었습니다.".formatted(post.getId()),
                new PostWriteResBody(
                        new PostDto(post),
                        totalCount
                )
        );

        // 2. 완성된 rsData 객체와 HTTP 상태 코드를 ResponseEntity에 담아 반환합니다.
        return new ResponseEntity<>(rsData, HttpStatus.CREATED);
    }

}
