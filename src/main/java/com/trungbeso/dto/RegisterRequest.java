package com.trungbeso.dto;

import com.trungbeso.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
	private String name;
	private String email;
	private String password;
	private Role role;
}
