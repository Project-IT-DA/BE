package com.example.itDa.domain.article.service;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.article.request.ArticleRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
@WebAppConfiguration
@SpringBootTest
@Transactional
class ArticleServiceTest {


    @Autowired
    ArticleService articleService;

    ArticleRequestDto requestDto = null;

    @BeforeEach
    void beforeEach() {
        // 테스트를 시작 할 때 마다 Article를 새로 생성
        // id와 updateDate는 자동 생성
        requestDto = new ArticleRequestDto();
        requestDto.setArticleName("팔아요");
        requestDto.setSubstance("팔아요");
        requestDto.setItemImg("1234");
        requestDto.setLocation("신사역");
        requestDto.setCategory(Category.PC);
        requestDto.setSellPrice(10000);
    }

    @Test
    void 거래글_작성() {
        //given
        articleService.registerArticle(requestDto);
        assertThat(articleService.registerArticle(requestDto)).isEqualTo(1);


        //when
        //then
    }

    @Test
    void 전체글조회() {
    }

    @Test
    void 특정글_조회() {
    }
}