package com.example.notice_board_crud.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 기본 키 매핑 - IDENTITY 전략
    private Long id;

    private String name;
    private String email;
    private String provider;  // OAuth 제공자
}
