package com.br.equaly.auth.app.service;

import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.auth.app.model.dto.refresh.RefreshSessionRequestDTO;
import com.br.equaly.auth.app.model.entity.RecoveryToken;
import com.br.equaly.auth.app.model.entity.RefreshToken;
import com.br.equaly.auth.app.model.entity.User;
import com.br.equaly.auth.app.model.vo.login.CorporationLoginVO;
import com.br.equaly.auth.app.model.vo.login.DepartmentLoginVO;
import jakarta.transaction.Transactional;

import java.util.Map;

public interface TokenService {
    Map<String, String> generateToken(User user, CorporationLoginVO corporationInformations, DepartmentLoginVO departmentInformations);
    String generateRefreshToken(String tokenId, User user, CorporationLoginVO corporationInformations, DepartmentLoginVO departmentInformations);
    RefreshToken getRefreshToken(RefreshSessionRequestDTO requestDTO);
    Map<String, Claim> getTokenInformations(String token);

    RefreshToken getRefreshToken(String sessionId);

    @Transactional
    void removeRefreshSession(String sessionId);

    @Transactional
    void removeRecoverySession(String sessionId);

    String getTokenSubject(String token);

    String generateRecoveryToken(User user);

    RecoveryToken getRecoveryToken(String tokenId);
}
