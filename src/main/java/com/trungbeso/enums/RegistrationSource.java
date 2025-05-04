package com.trungbeso.enums;

import java.util.Arrays;

public enum RegistrationSource {
	GOOGLE, GITHUB, FACEBOOK;

	public static RegistrationSource fromRegistrationId(String registrationId) {
		return Arrays.stream(values())
			  .filter(source -> source.name().equalsIgnoreCase(registrationId))
			  .findFirst()
			  .orElseThrow(() -> new IllegalArgumentException("Invalid registration ID"));
	}
}
