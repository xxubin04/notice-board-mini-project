package com.example.notice_board_crud.controller;

import com.example.notice_board_crud.entity.Post;
import com.example.notice_board_crud.entity.User;
import com.example.notice_board_crud.repository.UserRepository;
import com.example.notice_board_crud.security.JwtTokenProvider;
import com.example.notice_board_crud.service.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5500", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final PostService postService;

    // JWT 반환 (BE -> FE)
    @GetMapping("/login-success")
    public ResponseEntity<Map<String, String>> loginSuccess(HttpServletResponse response, OAuth2AuthenticationToken authToken) throws IOException {
        OAuth2User user = authToken.getPrincipal();
        String email = user.getAttribute("email");
        Optional<User> userInfo = userRepository.findByEmail(email);

        if (userInfo.isPresent()) {
            // JSON에 Access Token 담기
            String token = jwtTokenProvider.generateToken(email);
            return ResponseEntity.ok(Map.of("token", token));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }
    }

    // JWT 인증 처리 (FE -> BE)
    @PostMapping
    public ResponseEntity<Post> createPost(@RequestHeader("Authorization") String token, @RequestBody Post post) {
        String email = jwtTokenProvider.validateToken(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        post.setAuthor(user);
        Post created = postService.createPost(post);
        return ResponseEntity.ok(created);
    }
}
