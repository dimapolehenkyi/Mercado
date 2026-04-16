package com.example.mercado.courses.course.entity;

import com.example.mercado.common.entity.BaseEntity;
import com.example.mercado.common.exception.AppException;
import com.example.mercado.common.exception.ErrorCode;
import com.example.mercado.courses.course.enums.CourseAccessType;
import com.example.mercado.courses.course.enums.CourseLevel;
import com.example.mercado.courses.course.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@ToString
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(
        name = "courses",
        indexes = {
                @Index(
                        name = "idx_teacher_id",
                        columnList = "teacher_id"
                ),
                @Index(
                        name = "idx_course_status",
                        columnList = "status"
                ),
                @Index(
                        name = "idx_course_deleted",
                        columnList = "deleted"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_name",
                        columnNames = "name"
                )
        }
)
public class Course extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Version
    private Long version;

    @Column(name = "user_id")
    @Setter
    private Long userId;

    @Column(name = "teacher_id")
    @Setter
    private Long teacherId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 2000, nullable = false)
    @Setter
    private String description;

    @Column(name = "short_description", length = 400, nullable = false)
    @Setter
    private String shortDescription;

    @Column(name = "preview_video_url")
    @Setter
    private String previewVideoUrl;

    @Column(name = "thumbnail_url")
    @Setter
    private String thumbnailUrl;

    @Builder.Default
    @Setter
    @Column(name = "student_count", nullable = false)
    private Long studentCount = 0L;

    @Column(name = "price",  nullable = false)
    private BigDecimal price;

    @Setter
    @Column(name = "duration_in_minutes")
    private Integer durationInMinutes;

    @Builder.Default
    @Column(name = "rating")
    private Double rating = 0.0;

    @Builder.Default
    @Column(name = "reviews_count")
    private Long reviewsCount = 0L;

    @Builder.Default
    @Column(name = "deleted",  nullable = false)
    private Boolean deleted = false;

    @Column(name = "is_free", nullable = false)
    @Setter
    private Boolean isFree;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status",  nullable = false)
    private CourseStatus status = CourseStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    @Setter
    private CourseAccessType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false)
    private CourseLevel level;



    public void setName(String name) {

        if (name == null || name.isBlank() || name.equals(this.name)) {
            throw new AppException(
                    ErrorCode.COURSE_NAME_INVALID
            );
        }

        this.name = name;
    }

    public void setStatus(CourseStatus status) {
        if (this.status == status) return;

        switch (this.status) {
            case DRAFT -> {
                if (status == CourseStatus.PUBLISHED || status == CourseStatus.ARCHIVED) {
                    this.status = status;
                    return;
                }
            }
            case PUBLISHED -> {
                if (status == CourseStatus.ARCHIVED || status == CourseStatus.CLOSED) {
                    this.status = status;
                    if (status == CourseStatus.CLOSED) deleted = true;
                    return;
                }
            }
            case ARCHIVED -> {
                if (status == CourseStatus.PUBLISHED || status == CourseStatus.CLOSED) {
                    this.status = status;
                    if (status == CourseStatus.CLOSED) deleted = true;
                    return;
                }
            }
        }
        throw new AppException(ErrorCode.COURSE_STATUS_INVALID);
    }

    public void setLevel(CourseLevel level) {
        if (level == null) {
            throw new AppException(
                    ErrorCode.COURSE_LEVEL_INVALID
            );
        }

        if (this.level == level) return;

        this.level = level;
    }

    public void applyPricing(CourseAccessType type, BigDecimal price) {
        if (type == null) {
            throw new AppException(
                    ErrorCode.COURSE_STATUS_INVALID
            );
        }

        if (type == CourseAccessType.FREE) {
            this.price = BigDecimal.ZERO;
            this.isFree = true;
            return;
        }

        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new AppException(ErrorCode.COURSE_PRICE_INVALID);
        }

        this.price = price;
        this.isFree = false;
    }
}
