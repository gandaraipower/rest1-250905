package com.rest1.domain.post.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Comment extends BaseEntity {

    private String content; //댓글

    @ManyToOne  //연관관계 맺기 , 현재 클래스 기준 댓글 여러개 원글 하나
    @JsonIgnore
    private Post post; //원글

    public void update(String content){
        this.content=content;
    }
}
