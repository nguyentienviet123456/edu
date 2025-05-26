package edu.vietgo.global.exam.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizDTO {
    private Long id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
    private String description;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 180, message = "Duration cannot be more than 180 minutes")
    private Integer duration; // in minutes

    @NotNull(message = "Total points cannot be null")
    @Min(value = 1, message = "Total points must be at least 1")
    private Integer totalPoints;

    @NotNull(message = "Passing score cannot be null")
    @Min(value = 0, message = "Passing score cannot be negative")
    private Integer passingScore;

    @NotNull(message = "Published status cannot be null")
    private Boolean isPublished = false;

    @NotEmpty(message = "Quiz must have at least one question")
    @Size(min = 1, max = 50, message = "Quiz must have between 1 and 50 questions")
    private List<QuizQuestionDTO> quizQuestions;
} 