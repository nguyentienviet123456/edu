package edu.vietgo.global.exam.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDTO {
    private Long id;

    @NotBlank(message = "Answer content cannot be empty")
    @Size(min = 1, max = 500, message = "Answer content must be between 1 and 500 characters")
    private String content;

    @NotNull(message = "Correct status cannot be null")
    private Boolean isCorrect;

    @NotNull(message = "Order cannot be null")
    @Min(value = 1, message = "Order must be at least 1")
    private Integer order;
} 