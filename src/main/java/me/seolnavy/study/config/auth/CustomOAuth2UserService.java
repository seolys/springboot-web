package me.seolnavy.study.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.seolnavy.study.config.auth.dto.OAuthAttributes;
import me.seolnavy.study.config.auth.dto.SessionUser;
import me.seolnavy.study.domain.user.User;
import me.seolnavy.study.domain.user.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId(); // 서비스구분코드(구글/네이버/카카오...)
        String userNameAttributeName = userRequest.getClientRegistration()
                                                  .getProviderDetails()
                                                  .getUserInfoEndpoint()
                                                  .getUserNameAttributeName(); // 로그인 진행시 KEY가 되는 필드(PK)
        Map<String, Object> userAttributes = oAuth2User.getAttributes(); // User정보.

        // OAuth DTO생성.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, userAttributes);

        User user = saveOrUpdate(attributes);
        httpSession.setAttribute("user", new SessionUser(user)); // User.class는 엔티티이기때문에, 직렬화를 구현한 별도의 dto를 세션에 세팅.
        log.debug("attributes.getNameAttributeKey() : " + attributes.getNameAttributeKey());
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                                    .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                                    .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}
