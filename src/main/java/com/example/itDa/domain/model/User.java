package com.example.itDa.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
@Table(name="users")
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @CreatedDate
    private String createdAt;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String password;

    @Column
    private int density;

    @OneToMany(mappedBy = "user")
    private List<Community> communityList = new ArrayList<Community>();

    @OneToMany(mappedBy = "user")
    private List<Comment> commentList = new ArrayList<Comment>();

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

}
