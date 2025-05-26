package edu.vietgo.global.exam.repository;

import edu.vietgo.global.exam.entity.Quiz;
import edu.vietgo.global.exam.entity.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    
    // Find all questions for a quiz
    List<QuizQuestion> findByQuiz(Quiz quiz);
    
    // Find all questions for a quiz ID
    List<QuizQuestion> findByQuizId(Long quizId);
    
    // Find questions by order
    List<QuizQuestion> findByQuizOrderByOrderAsc(Quiz quiz);
    
    // Find questions by order for a quiz ID
    List<QuizQuestion> findByQuizIdOrderByOrderAsc(Long quizId);
    
    // Find question by quiz and order
    QuizQuestion findByQuizAndOrder(Quiz quiz, Integer order);
    
    // Find question by quiz ID and order
    QuizQuestion findByQuizIdAndOrder(Long quizId, Integer order);
    
    // Get total points for a quiz
    @Query("SELECT SUM(qq.points) FROM QuizQuestion qq WHERE qq.quiz = :quiz")
    Integer getTotalPoints(@Param("quiz") Quiz quiz);
    
    // Get total points for a quiz ID
    @Query("SELECT SUM(qq.points) FROM QuizQuestion qq WHERE qq.quiz.id = :quizId")
    Integer getTotalPointsByQuizId(@Param("quizId") Long quizId);
    
    // Check if question exists in quiz
    boolean existsByQuizAndQuestionId(Quiz quiz, Long questionId);
    
    // Check if question exists in quiz by IDs
    boolean existsByQuizIdAndQuestionId(Long quizId, Long questionId);
} 