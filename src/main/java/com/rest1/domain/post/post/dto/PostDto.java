package com.rest1.domain.post.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class PostDto {
    private Long id;
    private LocalDateTime createDate;
    private LocalDateTime modifyDate;
    private String title;
    private String content;


}
