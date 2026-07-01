package com.example.mercado.courses.module.entity;

import com.example.mercado.common.entity.BaseEntity;
import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.module.enums.ModuleStatus;
import com.example.mercado.courses.module.enums.ModuleAccessType;
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
        indexes = {
                @Index(
                        name = "idx_module_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_module_course_id",
                        columnList = "course_id"
                ),
                @Index(
                        name = "idx_module_course_position",
                        columnList = "course_id, position"
                )
        },
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
public class Module extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_id", nullable = false)
    @Setter
    private Long courseId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "description", length = 1000)
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
    @Builder.Default
    private ModuleStatus status = ModuleStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Setter
    @Builder.Default
    private ModuleAccessType moduleAccessType = ModuleAccessType.FREE;


    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new AppException(
                    ErrorCode.MODULE_NAME_INVALID
            );
        }

        this.name = name;
    }

    public void setStatus(ModuleStatus status) {
        if (this.status == status) return;

        switch (this.status) {
            case DRAFT -> {
                if (status == ModuleStatus.PUBLISHED || status == ModuleStatus.ARCHIVED) {
                    this.status = status;
                    return;
                }
            }
            case PUBLISHED -> {
                if (status == ModuleStatus.ARCHIVED || status == ModuleStatus.CLOSED || status == ModuleStatus.DRAFT) {
                    this.status = status;
                    if (status == ModuleStatus.CLOSED) deleted = true;
                    return;
                }
            }
            case ARCHIVED -> {
                if (status == ModuleStatus.PUBLISHED || status == ModuleStatus.CLOSED || status == ModuleStatus.DRAFT) {
                    this.status = status;
                    if (status == ModuleStatus.CLOSED) deleted = true;
                    return;
                }
            }
        }
        throw new AppException(ErrorCode.MODULE_STATUS_INVALID);
    }

}
