package edu.vietgo.global.exam.repository;

import edu.vietgo.global.exam.entity.Question;
import edu.vietgo.global.exam.entity.QuizAttempt;
import edu.vietgo.global.exam.entity.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {

    // Find answers by quiz attempt
    List<UserAnswer> findByQuizAttemptAndIsCorrectTrue(QuizAttempt quizAttempt);

    // Find answers by quiz attempt
    List<UserAnswer> findByQuizAttempt(QuizAttempt quizAttempt);
    
    // Find answers by quiz attempt ID
    List<UserAnswer> findByQuizAttemptId(Long quizAttemptId);
    
    // Find answers by question
    List<UserAnswer> findByQuestion(Question question);
    
    // Find answers by question ID
    List<UserAnswer> findByQuestionId(Long questionId);
    
    // Find correct answers
    List<UserAnswer> findByIsCorrectTrue();
    
    // Find incorrect answers
    List<UserAnswer> findByIsCorrectFalse();
    
    // Find answers by quiz attempt and question
    UserAnswer findByQuizAttemptAndQuestion(QuizAttempt quizAttempt, Question question);
    
    // Find answers by quiz attempt ID and question ID
    UserAnswer findByQuizAttemptIdAndQuestionId(Long quizAttemptId, Long questionId);
    
    // Get total points for a quiz attempt
    @Query("SELECT SUM(ua.points) FROM UserAnswer ua WHERE ua.quizAttempt = :quizAttempt")
    Integer getTotalPoints(@Param("quizAttempt") QuizAttempt quizAttempt);
    
    // Get total points for a quiz attempt ID
    @Query("SELECT SUM(ua.points) FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId")
    Integer getTotalPointsByQuizAttemptId(@Param("quizAttemptId") Long quizAttemptId);
    
    // Count correct answers for a quiz attempt
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.quizAttempt = :quizAttempt AND ua.isCorrect = true")
    long countCorrectAnswers(@Param("quizAttempt") QuizAttempt quizAttempt);
    
    // Count correct answers for a quiz attempt ID
    @Query("SELECT COUNT(ua) FROM UserAnswer ua WHERE ua.quizAttempt.id = :quizAttemptId AND ua.isCorrect = true")
    long countCorrectAnswersByQuizAttemptId(@Param("quizAttemptId") Long quizAttemptId);
    
    // Check if answer exists for a question in a quiz attempt
    boolean existsByQuizAttemptAndQuestion(QuizAttempt quizAttempt, Question question);
    
    // Check if answer exists for a question ID in a quiz attempt ID
    boolean existsByQuizAttemptIdAndQuestionId(Long quizAttemptId, Long questionId);
} 