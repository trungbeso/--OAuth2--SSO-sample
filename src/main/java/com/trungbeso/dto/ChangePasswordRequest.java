package com.trungbeso.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChangePasswordRequest {
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
}
