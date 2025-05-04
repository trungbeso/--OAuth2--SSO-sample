package com.trungbeso.services;

import com.trungbeso.entity.UserEntity;
import com.trungbeso.repositories.IUserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{

	private final IUserRepository userRepository;

	public UserService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserEntity save(UserEntity user) {
		return userRepository.save(user);
	}

	@Override
	public UserEntity findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
