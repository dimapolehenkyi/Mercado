package com.example.mercado.courses.module.entity;

import com.example.mercado.courses.module.common.base.BaseEntityModule;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modules_table")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Module extends BaseEntityModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id",  nullable = false)
    @Setter
    private Long courseId;

    @Column(name = "name",  nullable = false)
    @Setter
    private String name;

    @Column(name = "description", length = 1000)
    @Setter
    private String description;

    @Column(name = "position")
    @Setter
    private Integer position;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Setter
    private ModuleStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Setter
    private ModuleType moduleType;
}
