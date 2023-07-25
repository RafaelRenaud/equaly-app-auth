package com.br.equaly.auth.app.configuration;

import com.br.equaly.auth.app.exception.InvalidTokenException;
import com.br.equaly.auth.app.model.dto.error.ErrorHandlerDTO;
import com.br.equaly.auth.app.model.entity.SessionToken;
import com.br.equaly.auth.app.repository.UserRepository;
import com.br.equaly.auth.app.service.impl.TokenServiceImpl;
import com.br.equaly.auth.app.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityFilterConfiguration extends OncePerRequestFilter {

    @Autowired
    private TokenServiceImpl tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String bearerToken = getAuthorizationBearer(request);

            if(bearerToken != null){
                Authentication authentication = getAuthenticationContext(bearerToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        }catch (Exception e){
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            response.getOutputStream().print(new ObjectMapper().writeValueAsString(new ErrorHandlerDTO(HttpStatus.UNAUTHORIZED, e)));
            response.flushBuffer();
        }

    }

    private Authentication getAuthenticationContext(String bearerToken){
        String sessionId = tokenService.getTokenInformations(bearerToken)
                .get(Constants.EQUALY_JWT_ID).asString();
        String appkey = tokenService.getTokenInformations(bearerToken)
                .get(Constants.EQUALY_APPLICATION_KEY).asString();
        SessionToken sessionInformations = tokenService.getSessionToken(sessionId);

        if(appkey.equals(sessionInformations.getAppkey())){
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(sessionInformations.getUserRole()));
            authorities.add(new SimpleGrantedAuthority(sessionInformations.getUserSubRole()));
            return new AnonymousAuthenticationToken(sessionId, sessionInformations, authorities);
        }else{
            throw new InvalidTokenException();
        }
    }

    private String getAuthorizationBearer(HttpServletRequest request){
        String authorization = request.getHeader(Constants.EQUALY_TOKEN_NAME);
        if(authorization != null){
            return authorization.replace(Constants.EQUALY_TOKEN_PREFIX,"");
        }

        return null;
    }
}
