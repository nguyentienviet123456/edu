package edu.vietgo.global.exam.controller;

import edu.vietgo.global.exam.dto.UserAnswerDTO;
import edu.vietgo.global.exam.entity.QuizAttempt;
import edu.vietgo.global.exam.entity.QuizAttemptStatus;
import edu.vietgo.global.exam.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz-attempts")
@RequiredArgsConstructor
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @PostMapping("/quizzes/{quizId}/start")
    public ResponseEntity<QuizAttempt> startQuizAttempt(
            @PathVariable Long quizId,
            @RequestParam Long userId) {
        return ResponseEntity.ok(quizAttemptService.startQuizAttempt(quizId, userId));
    }

    @PutMapping("/{attemptId}/submit")
    public ResponseEntity<QuizAttempt> submitQuizAttempt(@PathVariable Long attemptId) {
        // Save all temporary answers before submitting
        quizAttemptService.saveAllAnswers(attemptId);
        return ResponseEntity.ok(quizAttemptService.submitQuizAttempt(attemptId));
    }

    @GetMapping("/{attemptId}")
    public ResponseEntity<QuizAttempt> getQuizAttempt(@PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttempt(attemptId));
    }

    @GetMapping("/{attemptId}/remaining-time")
    public ResponseEntity<Long> getRemainingTime(@PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getRemainingTime(attemptId));
    }

    @PutMapping("/{attemptId}/extend-time")
    public ResponseEntity<QuizAttempt> extendTime(
            @PathVariable Long attemptId,
            @RequestParam Integer additionalMinutes) {
        return ResponseEntity.ok(quizAttemptService.extendTime(attemptId, additionalMinutes));
    }

    @PostMapping("/{attemptId}/answers")
    public ResponseEntity<Void> saveTemporaryAnswer(
            @PathVariable Long attemptId,
            @RequestBody UserAnswerDTO answer) {
        quizAttemptService.saveTemporaryAnswer(attemptId, answer);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{attemptId}/temporary-answers")
    public ResponseEntity<Map<Long, UserAnswerDTO>> getTemporaryAnswers(@PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getTemporaryAnswers(attemptId));
    }

    @PostMapping("/{attemptId}/save-all-answers")
    public ResponseEntity<Void> saveAllAnswers(@PathVariable Long attemptId) {
        quizAttemptService.saveAllAnswers(attemptId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<List<UserAnswerDTO>> getUserAnswers(@PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getUserAnswers(attemptId));
    }

    @GetMapping("/{attemptId}/result")
    public ResponseEntity<Object> getQuizAttemptResult(@PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptResult(attemptId));
    }

    @GetMapping("/quizzes/{quizId}/statistics")
    public ResponseEntity<Object> getQuizStatistics(@PathVariable Long quizId) {
        return ResponseEntity.ok(quizAttemptService.getQuizStatistics(quizId));
    }

    @GetMapping("/users/{userId}/history")
    public ResponseEntity<Page<QuizAttempt>> getUserQuizHistory(
            @PathVariable Long userId,
            Pageable pageable) {
        return ResponseEntity.ok(quizAttemptService.getUserQuizHistory(userId, pageable));
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptsByDateRange(startDate, endDate));
    }

    @GetMapping("/score-range")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByScoreRange(
            @RequestParam Integer minScore,
            @RequestParam Integer maxScore) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptsByScoreRange(minScore, maxScore));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByStatus(
            @PathVariable QuizAttemptStatus status) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptsByStatus(status));
    }

    @GetMapping("/{attemptId}/review")
    public ResponseEntity<Object> getQuizAttemptReview(@PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptReview(attemptId));
    }

    @PostMapping("/{attemptId}/review")
    public ResponseEntity<Void> submitQuizAttemptReview(
            @PathVariable Long attemptId,
            @RequestBody Object review) {
        quizAttemptService.submitQuizAttemptReview(attemptId, review);
        return ResponseEntity.ok().build();
    }
} 