package com.rest1.domain.post.post.controller;

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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ApiV1PostControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    @DisplayName("글 다건 조회")
    void t1() throws Exception {
        ResultActions resultActions = mvc.
                perform(
                        get("/api/v1/posts")
                )
                .andDo(print());

        resultActions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("글 생성")
    void t2() throws Exception {

        String title = "새로운 제목입니다.";
        String content = "새로운 내용입니다.";

        ResultActions resultActions = mvc.
                perform(
                        post("/api/v1/posts") // post() import 필요
                                .contentType(MediaType.APPLICATION_JSON) // 데이터 형식이 JSON임을 명시
                                .content("""
                                        {
                                            "title": "%s",
                                            "content": "%s"
                                        }
                                        """.formatted(title, content))
                )
                .andDo(print());

        resultActions
                .andExpect(status().isCreated());
    }
}