package edu.vietgo.global.exam.service;

import edu.vietgo.global.exam.dto.UserAnswerDTO;
import edu.vietgo.global.exam.entity.Quiz;
import edu.vietgo.global.exam.entity.QuizAttempt;
import edu.vietgo.global.exam.entity.QuizAttemptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface QuizAttemptService {
    
    // Start a new quiz attempt
    QuizAttempt startQuizAttempt(Long quizId, Long userId);
    
    // Submit a quiz attempt
    QuizAttempt submitQuizAttempt(Long attemptId);
    
    // Get quiz attempt details
    QuizAttempt getQuizAttempt(Long attemptId);
    
    // Get remaining time
    Long getRemainingTime(Long attemptId);
    
    // Extend time for quiz attempt
    QuizAttempt extendTime(Long attemptId, Integer additionalMinutes);
    
    // Save temporary answer
    void saveTemporaryAnswer(Long attemptId, UserAnswerDTO answer);
    
    // Get temporary answers
    Map<Long, UserAnswerDTO> getTemporaryAnswers(Long attemptId);
    
    // Save all answers to database
    void saveAllAnswers(Long attemptId);
    
    // Get user answers for a quiz attempt
    List<UserAnswerDTO> getUserAnswers(Long attemptId);
    
    // Get quiz attempt result
    Object getQuizAttemptResult(Long attemptId);
    
    // Get quiz statistics
    Object getQuizStatistics(Long quizId);
    
    // Get user's quiz history
    Page<QuizAttempt> getUserQuizHistory(Long userId, Pageable pageable);
    
    // Get quiz attempts by date range
    List<QuizAttempt> getQuizAttemptsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    
    // Get quiz attempts by score range
    List<QuizAttempt> getQuizAttemptsByScoreRange(Integer minScore, Integer maxScore);
    
    // Get quiz attempts by status
    List<QuizAttempt> getQuizAttemptsByStatus(QuizAttemptStatus status);
    
    // Get quiz attempts by quiz and user
    List<QuizAttempt> getQuizAttemptsByQuizAndUser(Quiz quiz, Long userId);
    
    // Get quiz attempts by quiz and status
    List<QuizAttempt> getQuizAttemptsByQuizAndStatus(Quiz quiz, QuizAttemptStatus status);
    
    // Get average score for a quiz
    Double getAverageScore(Long quizId);
    
    // Get highest score for a quiz
    Integer getHighestScore(Long quizId);
    
    // Get quiz attempt review
    Object getQuizAttemptReview(Long attemptId);
    
    // Submit quiz attempt review
    void submitQuizAttemptReview(Long attemptId, Object review);
} 