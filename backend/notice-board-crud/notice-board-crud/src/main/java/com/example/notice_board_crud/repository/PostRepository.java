package com.example.notice_board_crud.repository;

import com.example.notice_board_crud.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
