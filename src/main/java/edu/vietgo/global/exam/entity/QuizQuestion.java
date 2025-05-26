package edu.vietgo.global.exam.entity;

import edu.vietgo.global.exam.entity.base.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quiz cannot be null")
    @ManyToOne
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @NotNull(message = "Question cannot be null")
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @NotNull(message = "Points cannot be null")
    @Min(value = 1, message = "Points must be at least 1")
    @Column(nullable = false)
    private Integer points;

    @NotNull(message = "Order cannot be null")
    @Min(value = 1, message = "Order must be at least 1")
    @Column(nullable = false)
    private Integer order;
} 