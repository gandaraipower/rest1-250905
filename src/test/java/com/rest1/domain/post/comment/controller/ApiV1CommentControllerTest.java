package com.rest1.domain.post.comment.controller;

import com.rest1.domain.post.comment.entity.Comment;
import com.rest1.domain.post.post.entity.Post;
import com.rest1.domain.post.post.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInRelativeOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1CommentControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("1번 글의 댓글 다건 조회")
    void t1() throws Exception {

        long targetPostId=1;
        ResultActions resultActions = mvc.
                perform(
                        get("/api/v1/posts/%d/comments".formatted(targetPostId))
                )
                .andDo(print());


        resultActions
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("getItems"))
                .andExpect(status().isOk());

        resultActions
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[*].id", containsInRelativeOrder(1, 3)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].createDate").exists())
                .andExpect(jsonPath("$[0].modifyDate").exists())
                .andExpect(jsonPath("$[0].content").value("댓글 1-1"));


    }


    @Test
    @DisplayName("1번 글의 댓글 단건 조회")
    void t2() throws Exception {

        long targetPostId=1;
        long targetCommentId=1;
        ResultActions resultActions = mvc.
                perform(
                        get("/api/v1/posts/%d/comments/%d".formatted(targetPostId, targetCommentId))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("getItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.createDate").exists())
                .andExpect(jsonPath("$.modifyDate").exists())
                .andExpect(jsonPath("$.content").value("댓글 1-1"));
    }

    @Test
    @DisplayName("1번 글의 댓글 생성")
    void t3() throws Exception {

        String content = "새로운 내용입니다.";
        long postId = 1;
        ResultActions resultActions = mvc.
                perform(
                        post("/api/v1/posts/%d/comments".formatted(postId)) // post() import 필요
                                .contentType(MediaType.APPLICATION_JSON) // 데이터 형식이 JSON임을 명시
                                .content("""
                                        {
                                            "content": "%s"
                                        }
                                        """.formatted( content))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("createItem"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.resultCode").value("201-1"))
                .andExpect(jsonPath("$.msg").value("7번 댓글이 생성되었습니다."))
                .andExpect(jsonPath("$.data.CommentDto.id").value(7))
                .andExpect(jsonPath("$.data.CommentDto.createDate").exists())
                .andExpect(jsonPath("$.data.CommentDto.modifyDate").exists())
                .andExpect(jsonPath("$.data.CommentDto.content").value(content));
    }

    @Test
    @DisplayName("1번 글의 1번 댓글 수정")
    void t4() throws Exception {

        long targetPostId = 1;
        long targetCommentId = 1;

        String content = "수정된 내용입니다.";

        ResultActions resultActions = mvc.
                perform(
                        put("/api/v1/posts/%d/comments/%d".formatted(targetPostId, targetCommentId))
                                .contentType(MediaType.APPLICATION_JSON) // 데이터 형식이 JSON임을 명시
                                .content("""
                                        {
                                            "content": "%s"
                                        }
                                        """.formatted(content))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("modifyItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 댓글이 수정되었습니다.".formatted(targetCommentId)));

        // 선택적 검증
        Post post = postRepository.findById(targetPostId).get();
        Comment comment =post.findCommentById(targetCommentId).get();

        assertThat(comment.getContent()).isEqualTo(content);
    }

    @Test
    @DisplayName("1번 글의 1번 댓글 삭제")
    void t5() throws Exception {

        long targetPostId = 1;
        long targetCommentId = 1;

        ResultActions resultActions = mvc.
                perform(
                        delete("/api/v1/posts/%d/comments/%d".formatted(targetPostId, targetCommentId))
                )
                .andDo(print());

        resultActions
                .andExpect(handler().handlerType(ApiV1CommentController.class))
                .andExpect(handler().methodName("deleteItem"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultCode").value("200-1"))
                .andExpect(jsonPath("$.msg").value("%d번 댓글이 삭제되었습니다.".formatted(targetCommentId)));

        Post post = postRepository.findById(targetPostId).orElse(null);
        Comment comment= post.findCommentById(targetCommentId).orElse(null);

        assertThat(comment).isNull();
    }

}
