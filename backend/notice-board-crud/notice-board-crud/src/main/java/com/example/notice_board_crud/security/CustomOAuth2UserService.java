package com.example.notice_board_crud.security;

import com.example.notice_board_crud.entity.User;
import com.example.notice_board_crud.repository.UserRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {

        // 사용자 정보 받아오기
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // Google에서 받은 사용자 정보 추출
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // DB에 사용자 등록 또는 조회
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setName(name);
                    newUser.setProvider(userRequest.getClientRegistration().getRegistrationId());
                    return userRepository.save(newUser);
                });

        // 별도의 JWT 발급 처리
        return oAuth2User;
    }
}
