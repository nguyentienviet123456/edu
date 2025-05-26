package edu.vietgo.global.exam.dto;

import edu.vietgo.global.exam.entity.QuizAttemptStatus;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizAttemptDTO {
    private Long id;

    @NotNull(message = "Quiz ID cannot be null")
    private Long quizId;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @Min(value = 0, message = "Score cannot be negative")
    private Integer score;

    @NotNull(message = "Status cannot be null")
    private QuizAttemptStatus status;

    private List<UserAnswerDTO> userAnswers;
} 