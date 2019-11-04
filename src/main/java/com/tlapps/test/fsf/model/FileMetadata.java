package com.tlapps.test.fsf.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Slf4j
@Data
@Entity
@Table(name = "FILE_METADATA")
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class FileMetadata extends BaseEntity implements Serializable {

    @Id
    @Column(name = "FILE_METADATA_ID", columnDefinition = "NUMBER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileMetadataId;

    @Column(name = "FILE_ID", unique = true)
    private String fileId;

    @Column(name = "ORIGINAL_FILE_NAME")
    private String originalFileName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User owner;

/*    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "USER_SESSION", referencedColumnName = "id")
    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<User> sharedAccess;*/


}
