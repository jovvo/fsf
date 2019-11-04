package com.tlapps.test.fsf.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;



@Slf4j
@Data
@Entity
@Table(name = "USER_SESSION")
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public final class UserSession extends BaseEntity implements Serializable {
    @Id
    @Column(name = "USER_SESSION_ID", columnDefinition = "NUMBER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userSessionId;

    @Column(name = "SESSION_TOKEN")
    private String sessionToken;

    @Column(name = "ATTRIBUTES")
    private String attributes;

/*    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_SESSION", referencedColumnName = "userId")
    private User user;*/
}
