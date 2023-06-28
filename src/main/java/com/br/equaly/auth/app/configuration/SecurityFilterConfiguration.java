package com.br.equaly.auth.app.configuration;

import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.auth.app.model.dto.refresh.RefreshSessionRequestDTO;
import com.br.equaly.auth.app.model.entity.RefreshToken;
import com.br.equaly.auth.app.repository.UserRepository;
import com.br.equaly.auth.app.service.impl.TokenServiceImpl;
import com.br.equaly.auth.app.util.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Configuration
public class SecurityFilterConfiguration extends OncePerRequestFilter {

    @Autowired
    private TokenServiceImpl tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String bearerToken = getToken(request);

        if(bearerToken != null){
            Map<String, Claim> tokenInformations = tokenService.getTokenInformations(bearerToken);
            RefreshToken sessionInformations = tokenService.getRefreshToken(tokenInformations.get(Constants.EQUALY_JWT_ID).asString());

            String login = sessionInformations.getLogin();
            UserDetails user = userRepository.findByLogin(login);

            Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request){
        String authorization = request.getHeader("Authorization");
        if(authorization != null){
            return authorization.replace("Bearer ","");
        }

        return null;
    }
}
