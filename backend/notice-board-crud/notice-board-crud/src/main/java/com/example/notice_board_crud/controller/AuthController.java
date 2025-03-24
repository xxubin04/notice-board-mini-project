package com.example.notice_board_crud.controller;

import com.example.notice_board_crud.entity.Post;
import com.example.notice_board_crud.entity.User;
import com.example.notice_board_crud.repository.UserRepository;
import com.example.notice_board_crud.security.JwtTokenProvider;
import com.example.notice_board_crud.service.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PostService postService;

    // 기본 생성자 필요 없음?
    @GetMapping("/login-success")
    public String loginSuccess(OAuth2AuthenticationToken authToken) {
        OAuth2User user = authToken.getPrincipal();
        String email = user.getAttribute("email");
        Optional<User> userInfo = userRepository.findByEmail(email);

        if (userInfo.isPresent()) {
            return jwtTokenProvider.generateToken(email);
        } else {
            return "Unauthorized";
        }
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        String email = jwtTokenProvider.validateToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        post.setAuthor(user);
        Post created = postService.createPost(post);
        return ResponseEntity.ok(created);
    }
}
