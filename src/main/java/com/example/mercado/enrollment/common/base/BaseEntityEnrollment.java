package com.example.mercado.enrollment.common.base;

import com.example.mercado.enrollment.enums.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntityEnrollment {

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(name = "enrolled_at", updatable = false)
    private LocalDateTime enrolledAt;

    @Setter(AccessLevel.NONE)
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private EnrollmentStatus status;
}
