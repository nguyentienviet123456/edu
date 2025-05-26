package edu.vietgo.global.exam.repository;

import edu.vietgo.global.exam.entity.Question;
import edu.vietgo.global.exam.entity.QuestionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // Find questions by category
    List<Question> findByCategory(String category);
    
    // Find questions by type
    List<Question> findByType(QuestionType type);
    
    // Find questions by difficulty level
    List<Question> findByDifficulty(Integer difficulty);
    
    // Find questions by category and type
    List<Question> findByCategoryAndType(String category, QuestionType type);
    
    // Find questions by category and difficulty
    List<Question> findByCategoryAndDifficulty(String category, Integer difficulty);
    
    // Search questions by content (partial match)
    @Query("SELECT q FROM Question q WHERE LOWER(q.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Question> searchByContent(@Param("keyword") String keyword);
    
    // Get questions with pagination
    Page<Question> findAll(Pageable pageable);
    
    // Get questions by category with pagination
    Page<Question> findByCategory(String category, Pageable pageable);
    
    // Get questions by type with pagination
    Page<Question> findByType(QuestionType type, Pageable pageable);
    
    // Get questions by difficulty with pagination
    Page<Question> findByDifficulty(Integer difficulty, Pageable pageable);
    
    // Check if question exists by content
    boolean existsByContent(String content);
} 