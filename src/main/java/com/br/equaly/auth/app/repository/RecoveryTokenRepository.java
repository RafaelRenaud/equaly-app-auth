package com.br.equaly.auth.app.repository;

import com.br.equaly.auth.app.model.entity.RecoveryToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryTokenRepository extends CrudRepository<RecoveryToken, String> {
}
