package com.example.mercado.courses.moduleResource.entity;

import com.example.mercado.courses.moduleResource.base.BaseEntityModuleResource;
import com.example.mercado.courses.moduleResource.enums.ModuleResourceType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Table(
        name = "module_resource",
        indexes = {
                @Index(
                        name = "idx_module_id",
                        columnList = "module_id"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_name_moduleid",
                        columnNames = {"name", "moduleId"}
                ),
                @UniqueConstraint(
                        name = "uk_position_moduleid",
                        columnNames = {"position", "moduleId"}
                )
        }
)
public class ModuleResource extends BaseEntityModuleResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module_id")
    @Setter
    private Long moduleId;

    @Column(
            name = "name",
            length = 150,
            nullable = false
    )
    @Setter
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Setter
    private ModuleResourceType type;

    @Column(
            name = "url",
            nullable = false,
            length = 500
    )
    @Setter
    private String url;

    @Column(
            name = "thumbnail_url",
            nullable = false,
            length = 500
    )
    @Setter
    private String thumbnailUrl;

    @Column(name = "position", nullable = false)
    @Setter
    private Integer position;

}
