package com.example.itDa.domain.model.article;

import com.example.itDa.domain.model.article.Category;
import com.example.itDa.domain.model.article.Status;
import com.example.itDa.domain.TimeStamped;
import com.example.itDa.dto.request.EditArticleRequestDto;
import com.example.itDa.domain.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Category category;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "article", fetch = FetchType.LAZY, cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonIgnore
    private List<Like> likeList = new ArrayList<>();



    public void update(EditArticleRequestDto editRequestDto) {
        this.articleName = editRequestDto.getArticleName();
        this.substance = editRequestDto.getSubstance();
        this.location = editRequestDto.getLocation();
        this.category = editRequestDto.getCategory();
        this.sellPrice = editRequestDto.getSellPrice();
    }

    public void updateStatus(Status status) {
        this.status = status;
    }
}
