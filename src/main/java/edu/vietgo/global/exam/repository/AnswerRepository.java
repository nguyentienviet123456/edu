package edu.vietgo.global.exam.repository;

import edu.vietgo.global.exam.entity.Answer;
import edu.vietgo.global.exam.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    
    // Find all answers for a question
    List<Answer> findByQuestion(Question question);
    
    // Find all answers for a question ID
    List<Answer> findByQuestionId(Long questionId);
    
    // Find correct answers for a question
    List<Answer> findByQuestionAndIsCorrectTrue(Question question);
    
    // Find correct answers for a question ID
    List<Answer> findByQuestionIdAndIsCorrectTrue(Long questionId);
    
    // Find answers by order
    List<Answer> findByQuestionOrderByOrderAsc(Question question);
    
    // Find answers by order for a question ID
    List<Answer> findByQuestionIdOrderByOrderAsc(Long questionId);
    
    // Check if answer exists for a question
    boolean existsByQuestionAndContent(Question question, String content);
    
    // Count correct answers for a question
    @Query("SELECT COUNT(a) FROM Answer a WHERE a.question = :question AND a.isCorrect = true")
    long countCorrectAnswers(@Param("question") Question question);
    
    // Count correct answers for a question ID
    @Query("SELECT COUNT(a) FROM Answer a WHERE a.question.id = :questionId AND a.isCorrect = true")
    long countCorrectAnswersByQuestionId(@Param("questionId") Long questionId);
} 