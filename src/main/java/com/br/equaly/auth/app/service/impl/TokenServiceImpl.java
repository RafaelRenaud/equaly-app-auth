package com.br.equaly.auth.app.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.auth.app.exception.InvalidTokenException;
import com.br.equaly.auth.app.model.dto.refresh.RefreshSessionRequestDTO;
import com.br.equaly.auth.app.model.entity.RecoveryToken;
import com.br.equaly.auth.app.model.entity.SessionToken;
import com.br.equaly.auth.app.model.entity.User;
import com.br.equaly.auth.app.model.enums.EmailTemplate;
import com.br.equaly.auth.app.model.vo.login.CorporationLoginVO;
import com.br.equaly.auth.app.model.vo.login.DepartmentLoginVO;
import com.br.equaly.auth.app.repository.RecoveryTokenRepository;
import com.br.equaly.auth.app.repository.SessionTokenRepository;
import com.br.equaly.auth.app.service.TokenService;
import com.br.equaly.auth.app.util.Constants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${server.security.tokenSecret}")
    private String tokenSecret;

    @Autowired
    private SessionTokenRepository sessionTokenRepository;

    @Autowired
    private RecoveryTokenRepository recoveryTokenRepository;

    @Override
    public Map<String, String> generateToken(User user, CorporationLoginVO corporationInformations, DepartmentLoginVO departmentInformations) {
        try {
            Map<String, String> tokenInformations = new HashMap<>();
            Algorithm algorithm = Algorithm.HMAC256(this.tokenSecret);
            String tokenId = UUID.randomUUID().toString();

            tokenInformations.put(Constants.EQUALY_JWT_ID, tokenId);

            tokenInformations.put(Constants.EQUALY_JWT_CONTENT,
                    JWT.create()
                            .withIssuer(Constants.EQUALY_ISSUER)
                            .withExpiresAt(generateExpirationDate())
                            .withIssuedAt(Date.from(Instant.now()))
                            .withJWTId(tokenId)
                            .withSubject(user.getEmail())
                            .withClaim(Constants.EQUALY_CORPORATION_NAME, corporationInformations.getName())
                            .withClaim(Constants.EQUALY_APPLICATION_KEY, corporationInformations.getAppkey())
                            .withClaim(Constants.EQUALY_USER_NAME, user.getName())
                            .withClaim(Constants.EQUALY_USER_ALIAS, user.getNickname())
                            .withClaim(Constants.EQUALY_USER_ROLE, user.getRole().toString())
                            .withClaim(Constants.EQUALY_USER_SUBROLE, user.getSubrole().toString())
                            .withClaim(Constants.EQUALY_USER_DEPARTMENT, departmentInformations.getName())
                            .sign(algorithm) ) ;

            return tokenInformations;

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token", exception);
        }
    }

    @Override
    public String generateSessionToken(String tokenId, User user, CorporationLoginVO corporationInformations, DepartmentLoginVO departmentInformations){
        SessionToken sessionTokenInformations = new SessionToken(
                tokenId,
                user.getLogin(),
                user.getEmail(),
                user.getRole().toString(),
                user.getSubrole().toString(),
                user.getDepartment().getId().toString(),
                departmentInformations.getName(),
                user.getCorporation().getId().toString(),
                corporationInformations.getAppkey()
        );

        return Base64.getEncoder().encodeToString(
                sessionTokenRepository.save(sessionTokenInformations)
                        .getId().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public SessionToken getSessionToken(RefreshSessionRequestDTO requestDTO){
        String tokenId = new String(Base64.getDecoder().decode(requestDTO.refreshToken()));
        return Optional.ofNullable(
                sessionTokenRepository.findById(tokenId).get()
        ).orElseThrow(InvalidTokenException::new);
    }

    @Override
    public SessionToken getSessionToken(String sessionId){
        return Optional.ofNullable(
                sessionTokenRepository.findById(sessionId).get()
        ).orElseThrow(InvalidTokenException::new);
    }

    @Override
    @Transactional
    public void removeSessionToken(String sessionId){
        sessionTokenRepository.deleteById(sessionId);
    }

    @Override
    @Transactional
    public void removeRecoverySession(String sessionId){
        recoveryTokenRepository.deleteById(sessionId);
    }

    @Override
    public String getTokenSubject(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.tokenSecret);
            return JWT.require(algorithm)
                    .withIssuer(Constants.EQUALY_ISSUER)
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception){
            throw new RuntimeException("Invalid JWT Token");
        }
    }

    @Override
    public Map<String, Claim> getTokenInformations(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.HMAC256(this.tokenSecret);
        return JWT.require(algorithm)
                .withIssuer(Constants.EQUALY_ISSUER)
                .build()
                .verify(token)
                .getClaims();
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    @Override
    public String generateRecoveryToken(User user){
        RecoveryToken recoveryToken = new RecoveryToken(
                UUID.randomUUID().toString(),
                EmailTemplate.ACCOUNT_RECOVERY,
                LocalDateTime.now().toString(),
                user.getName(),
                user.getLogin(),
                user.getEmail());

        return Base64.getEncoder().encodeToString(
                recoveryTokenRepository.save(recoveryToken).getId().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public RecoveryToken getRecoveryToken(String tokenId){
        tokenId = new String(Base64.getDecoder().decode(tokenId));
        RecoveryToken recoveryToken = Optional.ofNullable(recoveryTokenRepository.findById(tokenId).get())
                .orElseThrow(InvalidTokenException::new);

        return recoveryToken;
    }
}
