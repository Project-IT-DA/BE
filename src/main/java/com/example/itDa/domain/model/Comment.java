package com.example.itDa.domain.model;

import com.example.itDa.dto.request.CommentRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private String content;

    @JoinColumn(name = "commuId", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;


    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent();
    }
}