package com.example.itDa.domain.article.service;

import com.example.itDa.domain.article.Article;
import com.example.itDa.domain.article.repository.ArticleRepository;
import com.example.itDa.domain.article.request.ArticleRequestDto;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.article.response.ArticleResponseDto;
import com.example.itDa.domain.model.User;
import com.example.itDa.domain.repository.UserRepository;
import com.example.itDa.infra.global.dto.ResponseDto;
import com.example.itDa.infra.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    //거래글 작성
    @Transactional
    public ResponseDto<?> registerArticle(UserDetailsImpl userDetails,ArticleRequestDto requestDto) {

        User user = getUser(userDetails.getUser().getId());
        if(!article.getUser().equals(user)){
            throw new IllegalStateException("로그인이 필요합니다.");
        }

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

    public ResponseDto<?> editArticle(UserDetailsImpl userDetails, Long articleId, EditArticleRequestDto editRequestDto) {
        Article article = getArticle(articleId);
        User user = userDetails.getUser();
        if(!article.getUser().equals(user)){
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        article.update(editRequestDto);

        return ResponseDto.success("거래글 수정 완료");
    }

    Article getArticle(Long id) {
        return articleRepository.findById(id).orElseThrow((
        )-> new RuntimeException("존재하지 않는 거래글 ID입니다.");
//                new RuntimeException(ErrorCode.ARTICLE_NOT_FOUND_404));
    }

    User getUser(Long id){
        return userRepository.findById(id).orElseThrow(()->new RuntimeException("존재하지 않는 사용자 입니다."));
    }

    public ResponseDto<?> withdrawArticle(UserDetailsImpl userDetails, Long articleId) {
        Article article = getArticle(articleId);
        User user = getUser(userDetails.getUser().getId());
        if(!article.getUser().equals(user)){
            throw new IllegalStateException("로그인이 필요합니다.");
        }

        articleRepository.delete(article);
        return ResponseDto.success("삭제 완료");
    }
}
