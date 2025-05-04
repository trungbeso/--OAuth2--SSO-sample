package com.trungbeso.entity;

import com.trungbeso.enums.TokenType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.oauth2.core.OAuth2AccessToken;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Token {
	@Id
	@GeneratedValue
	Integer id;

	@Column(unique=true)
	String token;

	@Enumerated(EnumType.STRING)
	TokenType tokenType = TokenType.BEARER;

	boolean revoked;

	boolean expired;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id")
	UserEntity user;
}
