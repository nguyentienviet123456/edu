package edu.vietgo.global.exam.repository;

import edu.vietgo.global.exam.entity.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    
    // Find quizzes by title (partial match)
    @Query("SELECT q FROM Quiz q WHERE LOWER(q.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Quiz> searchByTitle(@Param("keyword") String keyword);
    
    // Find published quizzes
    List<Quiz> findByIsPublishedTrue();
    
    // Find unpublished quizzes
    List<Quiz> findByIsPublishedFalse();
    
    // Find quizzes by duration range
    @Query("SELECT q FROM Quiz q WHERE q.duration BETWEEN :minDuration AND :maxDuration")
    List<Quiz> findByDurationRange(@Param("minDuration") Integer minDuration, @Param("maxDuration") Integer maxDuration);
    
    // Find quizzes by total points range
    @Query("SELECT q FROM Quiz q WHERE q.totalPoints BETWEEN :minPoints AND :maxPoints")
    List<Quiz> findByPointsRange(@Param("minPoints") Integer minPoints, @Param("maxPoints") Integer maxPoints);
    
    // Get quizzes with pagination
    Page<Quiz> findAll(Pageable pageable);
    
    // Get published quizzes with pagination
    Page<Quiz> findByIsPublishedTrue(Pageable pageable);
    
    // Get unpublished quizzes with pagination
    Page<Quiz> findByIsPublishedFalse(Pageable pageable);
    
    // Check if quiz exists by title
    boolean existsByTitle(String title);
    
    // Count total questions in a quiz
    @Query("SELECT COUNT(qq) FROM QuizQuestion qq WHERE qq.quiz = :quiz")
    long countQuestions(@Param("quiz") Quiz quiz);
    
    // Count total questions in a quiz by ID
    @Query("SELECT COUNT(qq) FROM QuizQuestion qq WHERE qq.quiz.id = :quizId")
    long countQuestionsByQuizId(@Param("quizId") Long quizId);
} 