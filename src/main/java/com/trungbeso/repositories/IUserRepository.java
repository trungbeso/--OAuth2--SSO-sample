package com.trungbeso.repositories;

import com.trungbeso.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
}
