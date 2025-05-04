package com.trungbeso.config;

import com.trungbeso.entity.UserEntity;
import com.trungbeso.enums.RegistrationSource;
import com.trungbeso.enums.Role;
import com.trungbeso.services.IUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	//	@Value("${react.fontEnd-url}")
	private static final String fontEndUrl = "http://localhost:5173";

	private final IUserService userService;

	public OAuth2LoginSuccessHandler(IUserService userService) {
		this.userService = userService;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    Authentication authentication) throws ServletException, IOException {

		OAuth2AuthenticationToken oAuth2Token = (OAuth2AuthenticationToken) authentication;
		String clientRegId = oAuth2Token.getAuthorizedClientRegistrationId();

		List<String> supportClients = Arrays.asList("github", "google", "facebook");
		if (supportClients.contains(clientRegId)) {
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
			Map<String, Object> attributes = oAuth2User.getAttributes();

			String nameAttributeKey = "id";
			if ("google".equals(clientRegId)) {
				nameAttributeKey = "sub";
			}

			String email = attributes.getOrDefault("email", "").toString();
			String name = attributes.getOrDefault("name", "").toString();

			UserEntity user = userService.findByEmail(email);
			if (user == null) {
				createNewUser(email, name, clientRegId, attributes, nameAttributeKey);
			} else {
				updateAuthentication(user, attributes, nameAttributeKey, clientRegId);
			}
		}

		this.setAlwaysUseDefaultTargetUrl(true);
		this.setDefaultTargetUrl(fontEndUrl);
		super.onAuthenticationSuccess(request, response, authentication);
	}

	private void updateAuthentication(UserEntity user, Map<String, Object> attributes, String nameAttributeKey,
	                                  String clientRegId) {
		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(user.getRole().name());
		DefaultOAuth2User newUser = new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
		SecurityContextHolder.getContext().setAuthentication(
			  new OAuth2AuthenticationToken(newUser, authorities, clientRegId)
		);
	}

	private void createNewUser(String email, String name,
	                           String clientRegId,
	                           Map<String, Object> attributes,
	                           String nameAttributeKey) {
		UserEntity userEntity = new UserEntity();
		userEntity.setEmail(email);
		userEntity.setName(name);
		userEntity.setRole(Role.USER);
		userEntity.setSource(mapRegistrationSource(clientRegId));
		userService.save(userEntity);

		List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(userEntity.getRole().name());
		DefaultOAuth2User newUser = new DefaultOAuth2User(authorities, attributes, nameAttributeKey);
		SecurityContextHolder.getContext().setAuthentication(new OAuth2AuthenticationToken(newUser, authorities, clientRegId));
	}

	private RegistrationSource mapRegistrationSource(String clientRegId) {
		return switch (clientRegId) {
			case "facebook" -> RegistrationSource.FACEBOOK;
			case "google" -> RegistrationSource.GOOGLE;
			case "github" -> RegistrationSource.GITHUB;
			default -> throw new IllegalArgumentException("Unsupported registration provider");
		};
	}
}
