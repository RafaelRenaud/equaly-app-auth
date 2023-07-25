package com.br.equaly.auth.app.service;

import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.auth.app.model.dto.refresh.RefreshSessionRequestDTO;
import com.br.equaly.auth.app.model.entity.RecoveryToken;
import com.br.equaly.auth.app.model.entity.SessionToken;
import com.br.equaly.auth.app.model.entity.User;
import com.br.equaly.auth.app.model.vo.login.CorporationLoginVO;
import com.br.equaly.auth.app.model.vo.login.DepartmentLoginVO;
import jakarta.transaction.Transactional;

import java.util.Map;

public interface TokenService {
    Map<String, String> generateToken(User user, CorporationLoginVO corporationInformations, DepartmentLoginVO departmentInformations);
    String generateSessionToken(String tokenId, User user, CorporationLoginVO corporationInformations, DepartmentLoginVO departmentInformations);
    SessionToken getSessionToken(RefreshSessionRequestDTO requestDTO);
    Map<String, Claim> getTokenInformations(String token);

    SessionToken getSessionToken(String sessionId);

    @Transactional
    void removeSessionToken(String sessionId);

    @Transactional
    void removeRecoverySession(String sessionId);

    String getTokenSubject(String token);

    String generateRecoveryToken(User user);

    RecoveryToken getRecoveryToken(String tokenId);
}
