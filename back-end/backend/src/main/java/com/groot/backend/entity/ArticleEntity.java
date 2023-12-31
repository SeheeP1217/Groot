package com.groot.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.groot.backend.dto.response.ArticleListDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="articles")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userPK;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Long views;

    @Column(name = "share_status")
    private Boolean shareStatus;

    @Column(name = "share_region")
    private String shareRegion;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserEntity userEntity;

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ArticleImageEntity> articleImageEntityList;

    @OneToMany(mappedBy = "articleEntity", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<ArticleTagEntity> articleTagEntityList;

}
