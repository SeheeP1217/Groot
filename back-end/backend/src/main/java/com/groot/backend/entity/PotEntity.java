package com.groot.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "pots")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
public class PotEntity extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", insertable = false, updatable = false)
    private Long userId;

    @Column(name = "plant_id", insertable = false, updatable = false)
    private Long plantId;

    @Column(nullable = false)
    private String name;

    @Column(name = "img_path", nullable = false)
    private String imgPath;

    @Column(name = "sale_date", nullable = false)
    @CreationTimestamp
    private Date saleDate;

    @Column(name = "character_id")
    private Long characterId;

    @Column
    private Double temperature;

    @Column
    private int illuminance;

    @Column
    private Double humidity;

    @Column(name = "water_date")
    private Date waterDate;

    @Column(name = "pruning_date")
    private Date pruningDate;

    @Column(name = "nutrients_date")
    private Date nutrientsDate;

    @Column(nullable = true, columnDefinition = "TINYINT(1) DEFAULT FALSE")
    private Boolean share;

    @Column(nullable = true, columnDefinition = "TINYINT(1) DEFAULT TRUE")
    private Boolean survival;

    @Column(name = "plant_kr_name", nullable = false)
    private String plantKrName;

    @ManyToOne(targetEntity = UserEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private UserEntity userEntity;

    @OneToMany(mappedBy = "potEntity", cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private List<DiaryEntity> diaryEntities;

    @ManyToOne(targetEntity = PlantEntity.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "plant_id")
    @JsonBackReference
    private PlantEntity plantEntity;
}
