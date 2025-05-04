package com.trungbeso.repositories;

import com.trungbeso.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ITokenRepository extends JpaRepository<Token, Integer> {

	@Query(value = """
	select t from Token t inner join UserEntity u on t.user.id == u.id where u.id = :id and (t.expired = false or t.revoked = false )
""")
	List<Token> findAllValidTokenByUser(Integer id);

	Optional<Token> findByToken(String token);
}
