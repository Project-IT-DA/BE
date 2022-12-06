package com.example.itDa.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CommunityFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imgName;

    @Column
    private String imgUrl;

    @JoinColumn(name = "community_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

}
