package com.br.equaly.auth.app.model.entity;

import com.br.equaly.auth.app.model.enums.Role;
import com.br.equaly.auth.app.model.enums.Subrole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Table(name = "USER", schema = "USER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "ID")
public class User implements UserDetails, Serializable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CORPORATION_ID")
    private Corporation corporation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPARTMENT_ID")
    private Department department;

    @Column(name = "LOGIN")
    private String login;

    @Column(name = "NAME")
    private String name;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE")
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(name = "SUBROLE")
    private Subrole subrole;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @Column(name = "FIRST_ACCESS")
    private Boolean isFirstAccess;

    @Column(name = "LAST_LOGIN_AT")
    private LocalDateTime lastLogin;

    @Column(name = "AVATAR_ID")
    private String avatarId;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "DISABLED_AT")
    private LocalDateTime disabledAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !isFirstAccess;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive && !isFirstAccess;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

}
