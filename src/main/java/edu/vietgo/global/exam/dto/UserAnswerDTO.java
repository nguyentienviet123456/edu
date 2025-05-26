package edu.vietgo.global.exam.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserAnswerDTO {
    @NotNull
    private Long questionId;
    
    private Long answerId; // For multiple choice questions
    
    private String textAnswer; // For essay or short answer questions
    
    private Boolean isCorrect;
    
    private Integer points;
} 