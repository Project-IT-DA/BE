package com.example.itDa.domain.article;

import com.example.itDa.domain.Category;
import com.example.itDa.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String articleName;

    @Column(nullable = false)
    private String itemImg;

    @Column(nullable = false)
    private String substance;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private int sellPrice;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

}
