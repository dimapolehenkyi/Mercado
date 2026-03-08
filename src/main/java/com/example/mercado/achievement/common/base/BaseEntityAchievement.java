package com.example.mercado.achievement.common.base;

import com.example.mercado.achievement.enums.AchievementType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public abstract class BaseEntityAchievement {

    @CreatedDate
    @Setter(AccessLevel.NONE)
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "enrolled_at")
    private LocalDateTime enrolledAt;


    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    @Setter
    private AchievementType type;
}
