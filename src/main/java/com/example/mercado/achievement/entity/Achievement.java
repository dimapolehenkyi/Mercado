package com.example.mercado.achievement.entity;

import com.example.mercado.achievement.common.base.BaseEntityAchievement;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Table(name = "achievement_table")
public class Achievement extends BaseEntityAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "description")
    @Setter
    private String description;

    @Column(name = "icon_url")
    @Setter
    private String iconUrl;

    @Column(name = "points")
    @Setter
    private Integer points;

    @Column(name = "active")
    @Setter
    @Builder.Default
    private Boolean active = true;
}
