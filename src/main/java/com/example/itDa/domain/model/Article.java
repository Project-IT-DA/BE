package com.example.itDa.domain.model;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import com.example.itDa.domain.TimeStamped;
import com.example.itDa.dto.request.EditArticleRequestDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;



    public void update(EditArticleRequestDto editRequestDto) {
        this.articleName = editRequestDto.getArticleName();
        this.substance = editRequestDto.getSubstance();
        this.location = editRequestDto.getLocation();
        this.category = editRequestDto.getCategory();
        this.sellPrice = editRequestDto.getSellPrice();
    }
}
