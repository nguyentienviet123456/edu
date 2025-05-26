package edu.vietgo.global.exam.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuizQuestionDTO {
    private Long id;

    @NotNull(message = "Question ID cannot be null")
    private Long questionId;

    @NotNull(message = "Points cannot be null")
    @Min(value = 1, message = "Points must be at least 1")
    private Integer points;

    @NotNull(message = "Order cannot be null")
    @Min(value = 1, message = "Order must be at least 1")
    private Integer order;
} 