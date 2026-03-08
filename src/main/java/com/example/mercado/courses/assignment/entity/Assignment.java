package com.example.mercado.courses.assignment.entity;

import com.example.mercado.courses.assignment.common.base.BaseEntityAssignment;
import com.example.mercado.courses.assignment.enums.AssignmentStatus;
import com.example.mercado.courses.assignment.enums.AssignmentType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "assignments_table")
public class Assignment extends BaseEntityAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module_id")
    @Setter
    private Long module_id;

    @Column(name = "name")
    @Setter
    private String name;

    @Column(name = "description", length = 1000)
    @Setter
    private String description;

    @Column(name = "maxScore")
    @Setter
    private Integer maxScore;
}
