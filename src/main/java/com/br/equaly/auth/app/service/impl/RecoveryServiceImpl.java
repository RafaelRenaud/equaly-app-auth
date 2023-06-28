package com.br.equaly.auth.app.service.impl;

import com.br.equaly.auth.app.exception.CorporationNotFoundException;
import com.br.equaly.auth.app.exception.UserValidationException;
import com.br.equaly.auth.app.model.dto.recovery.RecoveryPasswordRequestDTO;
import com.br.equaly.auth.app.model.dto.recovery.RecoveryRequestDTO;
import com.br.equaly.auth.app.model.entity.RecoveryToken;
import com.br.equaly.auth.app.model.entity.User;
import com.br.equaly.auth.app.repository.UserRepository;
import com.br.equaly.auth.app.service.RecoveryService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RecoveryServiceImpl implements RecoveryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User verifyAccount(String login, String email, String domain) {
        UserDetails userDetails = userRepository.findByLogin(login);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        User user = (User) authentication.getPrincipal();

        if(user.getIsActive() && !user.getIsFirstAccess()
            && email.equals(user.getEmail()))
        {
            if(domain.equals(user.getCorporation().getAlias()) && user.getCorporation().getIsActive()){
                return user;
            }else{
                throw new CorporationNotFoundException();
            }
        }else {
            throw new UserValidationException();
        }
    }

    @Override
    public Boolean isValidAccount(RecoveryToken recoveryToken, RecoveryPasswordRequestDTO recoveryRequestDTO) {
        if(recoveryToken.getEmail().equals(recoveryRequestDTO.email())
        && recoveryToken.getLogin().equals(recoveryRequestDTO.login())){
            return true;
        }else{
            throw new UserValidationException();
        }
    }

    @Override
    @Transactional
    public void changePassword(RecoveryPasswordRequestDTO recoveryRequestDTO) {
        userRepository.updatePassword(recoveryRequestDTO.login(),
                passwordEncoder.encode(recoveryRequestDTO.password()));
    }
}
