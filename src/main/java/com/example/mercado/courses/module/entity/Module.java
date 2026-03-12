package com.example.mercado.courses.module.entity;

import com.example.mercado.courses.module.common.base.BaseEntityModule;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(
        name = "modules",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_module_name_courseid",
                        columnNames = {"name", "course_id"}
                ),
                @UniqueConstraint(
                        name = "uk_position_courseid",
                        columnNames = {"position", "course_id"}
                )
        }
)
public class Module extends BaseEntityModule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "course_id",
            nullable = false
    )
    @Setter
    private Long courseId;

    @Column(
            name = "name",
            nullable = false,
            length = 150
    )
    @Setter
    private String name;

    @Column(
            name = "description",
            length = 1000
    )
    @Setter
    private String description;

    @Column(name = "deleted", nullable = false)
    @Setter
    @Builder.Default
    private boolean deleted = false;

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
