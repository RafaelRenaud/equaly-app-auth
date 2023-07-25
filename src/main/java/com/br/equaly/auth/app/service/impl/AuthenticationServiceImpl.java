package com.br.equaly.auth.app.service.impl;

import com.auth0.jwt.interfaces.Claim;
import com.br.equaly.auth.app.exception.CorporationNotFoundException;
import com.br.equaly.auth.app.exception.DepartmentNotFoundException;
import com.br.equaly.auth.app.exception.UserValidationException;
import com.br.equaly.auth.app.model.dto.login.LoginResponseDTO;
import com.br.equaly.auth.app.model.entity.Corporation;
import com.br.equaly.auth.app.model.entity.Department;
import com.br.equaly.auth.app.model.entity.SessionToken;
import com.br.equaly.auth.app.model.entity.User;
import com.br.equaly.auth.app.model.vo.login.CorporationLoginVO;
import com.br.equaly.auth.app.model.vo.login.DepartmentLoginVO;
import com.br.equaly.auth.app.repository.CorporationRepository;
import com.br.equaly.auth.app.repository.UserRepository;
import com.br.equaly.auth.app.util.Constants;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CorporationRepository corporationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username);
    }

    public CorporationLoginVO checkCorporationAndAppkeyEnabled(String domain, Corporation corporation){
        CorporationLoginVO corporationLoginVO = null;

        if(corporation.getIsActive() && domain.equals(corporation.getAlias())){
            corporationLoginVO = new CorporationLoginVO(
                    corporation.getName(),
                    corporation.getAlias(),
                    corporation.getIsActive()
            );

            String appkey = Optional.ofNullable(corporationRepository.getValidAppkey(corporation.getId()))
                    .orElseThrow(CorporationNotFoundException::new);
            corporationLoginVO.setAppkey(appkey);
        }else{
            throw new CorporationNotFoundException();
        }

        return corporationLoginVO;
    }

    public CorporationLoginVO checkCorporationAndAppkeyEnabled(Long corporationId, String appkey, Corporation corporation){
        CorporationLoginVO corporationInformations = null;
        Boolean isValidAppkey = Boolean.valueOf(corporationRepository.checkValidAppkey(corporation.getId(), appkey));

        if(isValidAppkey && corporationId.equals(corporation.getId()) && corporation.getIsActive()){
            corporationInformations = new CorporationLoginVO(
                    corporation.getName(),
                    corporation.getAlias(),
                    corporation.getIsActive(),
                    appkey
            );
        }else{
            throw new CorporationNotFoundException();
        }

        return corporationInformations;
    }

    public DepartmentLoginVO checkDepartmentEnabled(Department department){
        DepartmentLoginVO departmentLoginVO = null;

        if(department.getIsActive()){
            departmentLoginVO = new DepartmentLoginVO(
                    department.getIsActive(),
                    department.getName()
            );
        }else{
            throw new DepartmentNotFoundException();
        }

        return departmentLoginVO;
    }

    public DepartmentLoginVO checkDepartmentEnabled(Long departmentSessionId, Department department){
        DepartmentLoginVO departmentLoginVO = null;

        if(department.getIsActive() && departmentSessionId.equals(department.getId())){
            departmentLoginVO = new DepartmentLoginVO(
                    department.getIsActive(),
                    department.getName()
            );
        }else{
            throw new DepartmentNotFoundException();
        }

        return departmentLoginVO;
    }

    @Transactional
    public void updateLastLogin(Long userId){
        userRepository.updateLastLogin(userId);
    }

    public LoginResponseDTO fetchAuthenticationObject(String token, String refreshToken){
        return new LoginResponseDTO(
                OffsetDateTime.now().toString(),
                UUID.randomUUID().toString(),
                token,
                refreshToken
        );
    }

    public User verifyUser(Map<String, Claim> accessToken, SessionToken sessionToken){
        User user = userRepository.findByLoginAndEmail(sessionToken.getLogin(), sessionToken.getEmail());
        String userTokenName = accessToken.get(Constants.EQUALY_USER_NAME).asString();
        String userTokenEmail = accessToken.get(Constants.EQUALY_JWT_SUBJECT).asString();

        if(user.getIsActive() && !user.getIsFirstAccess()
                && userTokenName.equals(user.getName())
                && userTokenEmail.equals(user.getEmail()) && userTokenEmail.equals(user.getEmail())
        ){
            return user;
        }else{
            throw new UserValidationException();
        }
    }
}
