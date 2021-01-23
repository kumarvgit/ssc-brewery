package guru.sfg.brewery.domain.security;

import guru.sfg.brewery.domain.Customer;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User  implements UserDetails, CredentialsContainer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;
    private String password;

//    @Singular
//    The singular annotation is used together with {@code @Builder} to create single element 'add' methods in the
//    builder for collections.
//    @ManyToMany(cascade = CascadeType.MERGE)
//    @JoinTable(name = "user_authority",
//            joinColumns = {
//                        @JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
//            inverseJoinColumns = {
//                        @JoinColumn(name = "AUTHORITY_ID", referencedColumnName = "ID")
//            })

    @Singular
    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role",
            joinColumns = {
                    @JoinColumn(name = "USER_ID", referencedColumnName = "ID")},
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID", referencedColumnName = "ID")
            })
    private Set<Role> roles;

    // adding customer data in custom implementation for UserDetailsService
    @ManyToOne(fetch = FetchType.EAGER)
    private Customer customer;


//    @Transient // this property is calculated
//    private Set<Authority> authorities;

    @Transient
    public Set<GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .map(authority -> {
                    return new SimpleGrantedAuthority(authority.getPermission());
                })
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Builder.Default
    private Boolean accountNonExpired = true;

    @Builder.Default
    private Boolean accountNonLocked = true;

    @Builder.Default
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    private Boolean enabled = true;

    @Override
    public void eraseCredentials() {
        this.password = null;
    }


    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;
}
