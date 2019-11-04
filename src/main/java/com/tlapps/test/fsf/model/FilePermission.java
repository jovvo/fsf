package com.tlapps.test.fsf.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.io.Serializable;

@Slf4j
@Data
@Entity
@Table(name = "FILE_PERMISIONS")
@ToString(callSuper = false)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class FilePermission extends BaseEntity implements Serializable {

    @Id
    @Column(name = "FILE_PERMISSION_ID", columnDefinition = "NUMBER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long filePermisionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "FILE_METADATA_ID", nullable = false)
    private FileMetadata file;

    @ManyToOne(optional = false)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User authorizedReader;

}
