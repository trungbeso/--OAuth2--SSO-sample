package com.trungbeso.services;

import com.trungbeso.entity.UserEntity;

public interface IUserService {
	UserEntity save(UserEntity user);

	UserEntity findByEmail(String email);
}
