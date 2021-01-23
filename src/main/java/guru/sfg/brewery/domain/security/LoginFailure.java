package guru.sfg.brewery.domain.security;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginFailure {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String username;

    @ManyToOne
    private User user;

    private String sourceIp;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDate;

    @UpdateTimestamp
    private Timestamp lastModifiedDate;

}
