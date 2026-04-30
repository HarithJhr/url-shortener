package com.harith.urlshortener.service;

import com.harith.urlshortener.model.User;
import com.harith.urlshortener.repository.UserRepository;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) {

        OidcUser oidcUser = super.loadUser(userRequest);

        String googleId = oidcUser.getSubject();
        String email = oidcUser.getEmail();
        String name = oidcUser.getFullName();
        String pictureUrl = oidcUser.getPicture();

        User user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> new User(googleId, email, name, pictureUrl));

        user.updateProfile(name, pictureUrl);
        userRepository.save(user);

        return oidcUser;
    }
}