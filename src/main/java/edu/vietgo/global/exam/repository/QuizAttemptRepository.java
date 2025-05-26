package edu.vietgo.global.exam.repository;

import edu.vietgo.global.exam.entity.Quiz;
import edu.vietgo.global.exam.entity.QuizAttempt;
import edu.vietgo.global.exam.entity.QuizAttemptStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    
    // Find attempts by quiz
    List<QuizAttempt> findByQuiz(Quiz quiz);
    
    // Find attempts by quiz ID
    List<QuizAttempt> findByQuizId(Long quizId);
    
    // Find attempts by user ID
    List<QuizAttempt> findByUserId(Long userId);
    
    // Find attempts by status
    List<QuizAttempt> findByStatus(QuizAttemptStatus status);
    
    // Find attempts by quiz and user
    List<QuizAttempt> findByQuizAndUserId(Quiz quiz, Long userId);
    
    // Find attempts by quiz ID and user ID
    List<QuizAttempt> findByQuizIdAndUserId(Long quizId, Long userId);
    
    // Find attempts by quiz and status
    List<QuizAttempt> findByQuizAndStatus(Quiz quiz, QuizAttemptStatus status);
    
    // Find attempts by quiz ID and status
    List<QuizAttempt> findByQuizIdAndStatus(Long quizId, QuizAttemptStatus status);
    
    // Find attempts by user ID and status
    List<QuizAttempt> findByUserIdAndStatus(Long userId, QuizAttemptStatus status);
    
    // Find attempts by date range
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.startTime BETWEEN :startDate AND :endDate")
    List<QuizAttempt> findByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Find attempts by score range
    @Query("SELECT qa FROM QuizAttempt qa WHERE qa.score BETWEEN :minScore AND :maxScore")
    List<QuizAttempt> findByScoreRange(@Param("minScore") Integer minScore, @Param("maxScore") Integer maxScore);
    
    // Get attempts with pagination
    Page<QuizAttempt> findAll(Pageable pageable);
    
    // Get attempts by quiz with pagination
    Page<QuizAttempt> findByQuiz(Quiz quiz, Pageable pageable);
    
    // Get attempts by user ID with pagination
    Page<QuizAttempt> findByUserId(Long userId, Pageable pageable);
    
    // Get attempts by status with pagination
    Page<QuizAttempt> findByStatus(QuizAttemptStatus status, Pageable pageable);
    
    // Get average score for a quiz
    @Query("SELECT AVG(qa.score) FROM QuizAttempt qa WHERE qa.quiz = :quiz")
    Double getAverageScore(@Param("quiz") Quiz quiz);
    
    // Get average score for a quiz ID
    @Query("SELECT AVG(qa.score) FROM QuizAttempt qa WHERE qa.quiz.id = :quizId")
    Double getAverageScoreByQuizId(@Param("quizId") Long quizId);
    
    // Get highest score for a quiz
    @Query("SELECT MAX(qa.score) FROM QuizAttempt qa WHERE qa.quiz = :quiz")
    Integer getHighestScore(@Param("quiz") Quiz quiz);
    
    // Get highest score for a quiz ID
    @Query("SELECT MAX(qa.score) FROM QuizAttempt qa WHERE qa.quiz.id = :quizId")
    Integer getHighestScoreByQuizId(@Param("quizId") Long quizId);
} 