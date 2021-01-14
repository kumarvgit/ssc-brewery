package guru.sfg.brewery.domain.security;

import lombok.*;

import javax.persistence.*;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class User {

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


    @Transient // this property is calculated
    private Set<Authority> authorities;

    public Set<Authority> getAuthorities() {
        return this.roles.stream()
                .map(Role::getAuthorities)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    @Builder.Default
    private Boolean accountNonExpired = true;

    @Builder.Default
    private Boolean accountNonLocked = true;

    @Builder.Default
    private Boolean credentialsNonExpired = true;

    @Builder.Default
    private Boolean enabled = true;

}
