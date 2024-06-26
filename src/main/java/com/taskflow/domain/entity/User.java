package com.taskflow.domain.entity;

import com.taskflow.config.context.TenantContext;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User implements UserDetails {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String organizationName;
    @Column(unique = true)
    private String personalEmail;
    private String email;
    private String password;
    private String phone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(
                name = "user_id"
        ),
        inverseJoinColumns = @JoinColumn(
                name = "role_id"
        )
    )
    private Set<Role> roles;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_group",
        joinColumns = @JoinColumn(
                name = "user_id"
        ),
        inverseJoinColumns = @JoinColumn(
                name = "group_id"
        )
    )
    private Set<PermissionGroup> permissionGroups;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        if(roles != null){
            roles.forEach(
                role ->
                    role.getPermissions().forEach(
                            permission -> authorities.add(
                                    new SimpleGrantedAuthority(permission.getSubject() + ":" + permission.getAction())
                            )
                    )
            );
        }
        if(permissionGroups != null){
            permissionGroups.forEach(
                permissionGroup ->
                    permissionGroup.getPermissions().forEach(
                            permission -> authorities.add(
                                    new SimpleGrantedAuthority(permission.getSubject() + ":" + permission.getAction())
                            )
                    )
            );
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
