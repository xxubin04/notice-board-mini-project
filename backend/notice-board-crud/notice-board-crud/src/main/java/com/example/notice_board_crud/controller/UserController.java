package com.example.notice_board_crud.controller;

import com.example.notice_board_crud.entity.User;
import com.example.notice_board_crud.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "사용자 생성")
    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User userCreated = userService.createUser(user);
        return ResponseEntity.ok(userCreated);
    }

    @Operation(summary = "전체 사용자 조회")
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @Operation(summary = "ID로 사용자 조회")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {  // Opitonal 사용법 확인
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "사용자 정보 수정")
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @PathVariable User user) {
        User userUpdated = userService.updateUser(id, user);
        return ResponseEntity.ok(userUpdated);
    }

    @Operation(summary = "사용자 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
}
