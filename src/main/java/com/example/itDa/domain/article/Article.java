package com.example.itDa.domain.article;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import com.example.itDa.domain.TimeStamped;
import com.example.itDa.domain.article.request.EditArticleRequestDto;
import com.example.itDa.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Article extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String articleName;


    @Column(nullable = false)
    private String substance;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int sellPrice;

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

//    public Article(ArticleRequestDto requestDto) {
//        this.articleName = requestDto.getArticleName();
//        this.substance = requestDto.getSubstance();
//        this.itemImg = requestDto.getItemImg();
//        this.location = requestDto.getLocation();
//        this.category = requestDto.getCategory();
//        this.sellPrice = requestDto.getSellPrice();
//    }

    public void update(EditArticleRequestDto editRequestDto) {
        this.articleName = editRequestDto.getArticleName();
        this.substance = editRequestDto.getSubstance();
        this.location = editRequestDto.getLocation();
        this.category = editRequestDto.getCategory();
        this.sellPrice = editRequestDto.getSellPrice();
    }
}
