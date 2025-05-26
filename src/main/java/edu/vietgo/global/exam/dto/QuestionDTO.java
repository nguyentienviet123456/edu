package edu.vietgo.global.exam.dto;

import edu.vietgo.global.exam.entity.QuestionType;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDTO {
    private Long id;

    @NotBlank(message = "Question content cannot be empty")
    @Size(min = 10, max = 1000, message = "Question content must be between 10 and 1000 characters")
    private String content;

    @NotNull(message = "Question type cannot be null")
    private QuestionType type;

    @NotNull(message = "Difficulty level cannot be null")
    @Min(value = 1, message = "Difficulty must be at least 1")
    @Max(value = 5, message = "Difficulty cannot be more than 5")
    private Integer difficulty;

    @NotBlank(message = "Category cannot be empty")
    @Size(min = 2, max = 50, message = "Category must be between 2 and 50 characters")
    private String category;

    @NotEmpty(message = "Question must have at least one answer")
    @Size(min = 2, max = 6, message = "Question must have between 2 and 6 answers")
    private List<AnswerDTO> answers;
} 