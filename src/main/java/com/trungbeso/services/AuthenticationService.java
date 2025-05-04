package com.trungbeso.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trungbeso.dto.AuthenticationRequest;
import com.trungbeso.dto.AuthenticationResponse;
import com.trungbeso.dto.RegisterRequest;
import com.trungbeso.entity.Token;
import com.trungbeso.entity.UserEntity;
import com.trungbeso.enums.TokenType;
import com.trungbeso.repositories.ITokenRepository;
import com.trungbeso.repositories.IUserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final IUserRepository userRepository;
	private final ITokenRepository tokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(RegisterRequest req) {
		var user = UserEntity.builder()
			  .email(req.getEmail())
			  .password(passwordEncoder.encode(req.getPassword()))
			  .role(req.getRole())
			  .name(req.getName())
			  .build();

		var savedUser = userRepository.save(user);
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		saveUserToken(savedUser, jwtToken);
		return AuthenticationResponse.builder()
			  .accessToken(jwtToken)
			  .refreshToken(refreshToken)
			  .build();
	}

	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
			  new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
		);
		var user = userRepository.findByEmail(request.getEmail());
		var jwtToken = jwtService.generateToken(user);
		var refreshToken = jwtService.generateRefreshToken(user);
		revokedAllUserTokens(user);
		saveUserToken(user, jwtToken);
		return AuthenticationResponse.builder()
			  .accessToken(jwtToken)
			  .refreshToken(refreshToken)
			  .build();
	}

	public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
		final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
		final String refreshToken;
		final String userEmail;
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			return;
		}
		refreshToken = authHeader.substring(7);
		userEmail = jwtService.extractUsername(refreshToken);
		if (userEmail != null) {
			var user = this.userRepository.findByEmail(userEmail);
			if (jwtService.isTokenValid(refreshToken, user)) {
				var accessToken = jwtService.generateToken(user);
				revokedAllUserTokens(user);
				saveUserToken(user, accessToken);
				var authResponse = AuthenticationResponse.builder()
					  .accessToken(accessToken)
					  .refreshToken(refreshToken)
					  .build();
				new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
			}
		}
	}

	private void saveUserToken(UserEntity user, String jwtToken) {
		var token = Token.builder()
			  .user(user)
			  .token(jwtToken)
			  .tokenType(TokenType.BEARER)
			  .expired(false)
			  .revoked(false)
			  .build();
		tokenRepository.save(token);
	}

	private void revokedAllUserTokens(UserEntity user) {
		var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		if (validUserTokens.isEmpty()) {
			return;
		}
		validUserTokens.forEach(token -> {
			token.setExpired(true);
			token.setRevoked(true);
		});
		tokenRepository.saveAll(validUserTokens);
	}
}
