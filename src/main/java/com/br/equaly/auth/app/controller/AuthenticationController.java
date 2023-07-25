package com.br.equaly.auth.app.controller;

import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.auth.app.model.dto.login.LoginRequestDTO;
import com.br.equaly.auth.app.model.dto.login.LoginResponseDTO;
import com.br.equaly.auth.app.model.dto.refresh.RefreshSessionRequestDTO;
import com.br.equaly.auth.app.model.entity.SessionToken;
import com.br.equaly.auth.app.model.entity.User;
import com.br.equaly.auth.app.model.vo.login.CorporationLoginVO;
import com.br.equaly.auth.app.model.vo.login.DepartmentLoginVO;
import com.br.equaly.auth.app.service.impl.AuthenticationServiceImpl;
import com.br.equaly.auth.app.service.impl.TokenServiceImpl;
import com.br.equaly.auth.app.util.Constants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenServiceImpl tokenService;



    @PostMapping("/login")
    public ResponseEntity login(
            @RequestBody
            @Valid
                    LoginRequestDTO loginRequestDTO
    ){

        UsernamePasswordAuthenticationToken authenticationInformations = new UsernamePasswordAuthenticationToken(
                loginRequestDTO.login(), loginRequestDTO.password());
        Authentication authentication = authenticationManager.authenticate(authenticationInformations);
        User user = (User) authentication.getPrincipal();

        CorporationLoginVO corporationInformations = authenticationService.checkCorporationAndAppkeyEnabled(loginRequestDTO.domain(), user.getCorporation());
        DepartmentLoginVO departmentInformations = authenticationService.checkDepartmentEnabled(user.getDepartment());

        if(user != null && corporationInformations != null && departmentInformations != null){
            Map<String, String> tokenInformations = tokenService.generateToken(user, corporationInformations,departmentInformations);
            String refreshToken = tokenService.generateSessionToken(tokenInformations.get(Constants.EQUALY_JWT_ID), user, corporationInformations, departmentInformations);
            authenticationService.updateLastLogin(user.getId());
            LoginResponseDTO loginResponseDTO = authenticationService.fetchAuthenticationObject(
                    tokenInformations.get(Constants.EQUALY_JWT_CONTENT), refreshToken);
            return ResponseEntity.ok(loginResponseDTO);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/refresh")
    public ResponseEntity refreshSession(
            @RequestHeader(Constants.EQUALY_TOKEN_NAME)
                    String authorization,
            @RequestBody
            @Valid
                    RefreshSessionRequestDTO refreshSessionRequestDTO

    ){
        authorization = authorization.replace(Constants.EQUALY_TOKEN_PREFIX, "");
        Map<String, Claim> tokenInformations = tokenService.getTokenInformations(authorization);
        SessionToken sessionToken = (SessionToken) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User validUser = authenticationService.verifyUser(tokenInformations, sessionToken);
        DepartmentLoginVO departmentInformations = authenticationService.checkDepartmentEnabled(
                Long.parseLong(sessionToken.getDepartmentId()),
                validUser.getDepartment());
        CorporationLoginVO corporationInformations = authenticationService.checkCorporationAndAppkeyEnabled(
                Long.parseLong(sessionToken.getCorporationId()),
                sessionToken.getAppkey(),
                validUser.getCorporation()
        );

        if(validUser !=null && departmentInformations !=null && corporationInformations !=null){
            Map<String, String> newToken = tokenService.generateToken(validUser, corporationInformations,departmentInformations);
            String newRefreshToken = tokenService.generateSessionToken(newToken.get(Constants.EQUALY_JWT_ID), validUser, corporationInformations, departmentInformations);
            tokenService.removeSessionToken(sessionToken.getId());
            LoginResponseDTO loginResponseDTO = authenticationService.fetchAuthenticationObject(
                    newToken.get(Constants.EQUALY_JWT_CONTENT), newRefreshToken);
            return ResponseEntity.ok(loginResponseDTO);
        }else{
            return ResponseEntity.badRequest().build();
        }
    }
}
