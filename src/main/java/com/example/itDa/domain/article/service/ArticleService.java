package com.example.itDa.domain.article.service;

import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.article.repository.ArticleRepository;
import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.response.ArticleResponseDto;
import com.example.itDa.infra.global.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    //거래글 작성
    @Transactional
    public ResponseDto<?> registerArticle(ArticleRequestDto requestDto) {
        Article registerArticle = articleRepository.save(
                Article.builder()
                        .articleName(requestDto.getArticleName())
                        .sellPrice(requestDto.getSellPrice())
                        .substance(requestDto.getSubstance())
                        .location(requestDto.getLocation())
                        .category(requestDto.getCategory())
                        .itemImg(requestDto.getItemImg())
                        .build()
        );
//        Article article = new Article(registerArticle);
//        article.updateStatus(Status.SELL);
        return ResponseDto.success(new ArticleResponseDto(registerArticle));
    }

    public ResponseDto<?> viewAllArticle() {
        List<Article> articleList = articleRepository.findAllByOrderByCreatedAtDesc();
        List<ArticleResponseDto> responses = new ArrayList<>();

        for (int i = 0; i < articleList.size(); i++) {
            Article article = articleList.get(i);
            ArticleResponseDto articleResponseDto = new ArticleResponseDto(article);
            responses.add(articleResponseDto);
        }
        return ResponseDto.success(articleList);


    }

    public ResponseDto<?> viewArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow((
        ) -> new IllegalArgumentException("존재하지 않는 게시글 아이디 입니다."));
        return ResponseDto.success(article);
    }
}
