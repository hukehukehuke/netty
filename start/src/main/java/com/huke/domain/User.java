package com.huke.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 * @author huke
 * @date 2022/08/26/下午3:35
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@With
@Table(name = "mooc_users")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, unique = true, nullable = false)
    private String username;
    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 255, nullable = false)
    private String matchPassword;

    @Column(length = 255, unique = true, nullable = false)
    private String email;

    @Column(length = 50)
    private String name;

    @Column(nullable = false)
    private boolean enabled;
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonLocked;
    @Column(name = "account_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "using_mfs", nullable = false)
    private boolean usingMfa = false;

    /**
     * 两部验证key
     */
    @JsonIgnore
    @Column(name = "mfa_key", nullable = false)
    private String mfaKey;

    @ManyToMany
    @Fetch(FetchMode.JOIN)
    @JoinTable(name = "mooc_user_roles",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}
    )
    private Set<Role> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }
}
