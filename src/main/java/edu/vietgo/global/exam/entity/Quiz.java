package edu.vietgo.global.exam.entity;

import edu.vietgo.global.exam.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.ArrayList;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quizzes")
public class Quiz extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Integer duration; // in minutes

    @Column(name = "total_points", nullable = false)
    private Integer totalPoints;

    @Column(name = "passing_score", nullable = false)
    private Integer passingScore;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = false;

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<QuizQuestion> quizQuestions = new ArrayList<>();
} 