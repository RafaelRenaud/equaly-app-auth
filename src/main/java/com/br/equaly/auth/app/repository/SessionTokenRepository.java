package com.br.equaly.auth.app.repository;

import com.br.equaly.auth.app.model.entity.SessionToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionTokenRepository extends CrudRepository<SessionToken, String> {
}
