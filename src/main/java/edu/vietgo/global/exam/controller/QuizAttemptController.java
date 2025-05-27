package edu.vietgo.global.exam.controller;

import edu.vietgo.global.exam.dto.UserAnswerDTO;
import edu.vietgo.global.exam.entity.QuizAttempt;
import edu.vietgo.global.exam.entity.QuizAttemptStatus;
import edu.vietgo.global.exam.service.QuizAttemptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Quiz Attempt", description = "Quiz attempt management APIs")
public class QuizAttemptController {

    private final QuizAttemptService quizAttemptService;

    @Operation(summary = "Start a new quiz attempt", description = "Creates a new quiz attempt for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz attempt started successfully",
            content = @Content(schema = @Schema(implementation = QuizAttempt.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @PostMapping("/quizzes/{quizId}/start")
    public ResponseEntity<QuizAttempt> startQuizAttempt(
            @Parameter(description = "ID of the quiz to attempt") @PathVariable Long quizId,
            @Parameter(description = "ID of the user attempting the quiz") @RequestParam Long userId) {
        return ResponseEntity.ok(quizAttemptService.startQuizAttempt(quizId, userId));
    }

    @Operation(summary = "Submit a quiz attempt", description = "Submits a quiz attempt and calculates the final score")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz attempt submitted successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found"),
        @ApiResponse(responseCode = "400", description = "Quiz attempt is not in progress")
    })
    @PutMapping("/{attemptId}/submit")
    public ResponseEntity<QuizAttempt> submitQuizAttempt(
            @Parameter(description = "ID of the quiz attempt to submit") @PathVariable Long attemptId) {
        quizAttemptService.saveAllAnswers(attemptId);
        return ResponseEntity.ok(quizAttemptService.submitQuizAttempt(attemptId));
    }

    @Operation(summary = "Get quiz attempt details", description = "Retrieves details of a specific quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz attempt found"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found")
    })
    @GetMapping("/{attemptId}")
    public ResponseEntity<QuizAttempt> getQuizAttempt(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttempt(attemptId));
    }

    @Operation(summary = "Get remaining time", description = "Gets the remaining time for a quiz attempt in seconds")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Remaining time retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found")
    })
    @GetMapping("/{attemptId}/remaining-time")
    public ResponseEntity<Long> getRemainingTime(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getRemainingTime(attemptId));
    }

    @Operation(summary = "Extend quiz time", description = "Extends the time limit for a quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Time extended successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found"),
        @ApiResponse(responseCode = "400", description = "Cannot extend time for non-in-progress attempt")
    })
    @PutMapping("/{attemptId}/extend-time")
    public ResponseEntity<QuizAttempt> extendTime(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId,
            @Parameter(description = "Additional minutes to extend") @RequestParam Integer additionalMinutes) {
        return ResponseEntity.ok(quizAttemptService.extendTime(attemptId, additionalMinutes));
    }

    @Operation(summary = "Save temporary answer", description = "Saves a temporary answer for a quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Answer saved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found"),
        @ApiResponse(responseCode = "400", description = "Cannot save answer for non-in-progress attempt")
    })
    @PostMapping("/{attemptId}/answers")
    public ResponseEntity<Void> saveTemporaryAnswer(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId,
            @Parameter(description = "Answer to save") @RequestBody UserAnswerDTO answer) {
        quizAttemptService.saveTemporaryAnswer(attemptId, answer);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get temporary answers", description = "Retrieves all temporary answers for a quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Temporary answers retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found")
    })
    @GetMapping("/{attemptId}/temporary-answers")
    public ResponseEntity<Map<Long, UserAnswerDTO>> getTemporaryAnswers(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getTemporaryAnswers(attemptId));
    }

    @Operation(summary = "Save all answers", description = "Saves all temporary answers to the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Answers saved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found")
    })
    @PostMapping("/{attemptId}/save-all-answers")
    public ResponseEntity<Void> saveAllAnswers(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId) {
        quizAttemptService.saveAllAnswers(attemptId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get user answers", description = "Retrieves all answers for a quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Answers retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found")
    })
    @GetMapping("/{attemptId}/answers")
    public ResponseEntity<List<UserAnswerDTO>> getUserAnswers(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getUserAnswers(attemptId));
    }

    @Operation(summary = "Get quiz attempt result", description = "Retrieves the result of a submitted quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Result retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found"),
        @ApiResponse(responseCode = "400", description = "Quiz attempt is not submitted")
    })
    @GetMapping("/{attemptId}/result")
    public ResponseEntity<Object> getQuizAttemptResult(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptResult(attemptId));
    }

    @Operation(summary = "Get quiz statistics", description = "Retrieves statistics for a specific quiz")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz not found")
    })
    @GetMapping("/quizzes/{quizId}/statistics")
    public ResponseEntity<Object> getQuizStatistics(
            @Parameter(description = "ID of the quiz") @PathVariable Long quizId) {
        return ResponseEntity.ok(quizAttemptService.getQuizStatistics(quizId));
    }

    @Operation(summary = "Get user quiz history", description = "Retrieves the quiz attempt history for a user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/users/{userId}/history")
    public ResponseEntity<Page<QuizAttempt>> getUserQuizHistory(
            @Parameter(description = "ID of the user") @PathVariable Long userId,
            @Parameter(description = "Pagination parameters") Pageable pageable) {
        return ResponseEntity.ok(quizAttemptService.getUserQuizHistory(userId, pageable));
    }

    @Operation(summary = "Get quiz attempts by date range", description = "Retrieves quiz attempts within a date range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz attempts retrieved successfully")
    })
    @GetMapping("/date-range")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByDateRange(
            @Parameter(description = "Start date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "End date") @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptsByDateRange(startDate, endDate));
    }

    @Operation(summary = "Get quiz attempts by score range", description = "Retrieves quiz attempts within a score range")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz attempts retrieved successfully")
    })
    @GetMapping("/score-range")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByScoreRange(
            @Parameter(description = "Minimum score") @RequestParam Double minScore,
            @Parameter(description = "Maximum score") @RequestParam Double maxScore) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptsByScoreRange(minScore, maxScore));
    }

    @Operation(summary = "Get quiz attempts by status", description = "Retrieves quiz attempts with a specific status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Quiz attempts retrieved successfully")
    })
    @GetMapping("/status/{status}")
    public ResponseEntity<List<QuizAttempt>> getQuizAttemptsByStatus(
            @Parameter(description = "Status of the quiz attempts") @PathVariable QuizAttemptStatus status) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptsByStatus(status));
    }

    @Operation(summary = "Get quiz attempt review", description = "Retrieves the review for a submitted quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found"),
        @ApiResponse(responseCode = "400", description = "Quiz attempt is not submitted")
    })
    @GetMapping("/{attemptId}/review")
    public ResponseEntity<Object> getQuizAttemptReview(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId) {
        return ResponseEntity.ok(quizAttemptService.getQuizAttemptReview(attemptId));
    }

    @Operation(summary = "Submit quiz attempt review", description = "Submits a review for a quiz attempt")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Review submitted successfully"),
        @ApiResponse(responseCode = "404", description = "Quiz attempt not found"),
        @ApiResponse(responseCode = "400", description = "Quiz attempt is not submitted")
    })
    @PostMapping("/{attemptId}/review")
    public ResponseEntity<Void> submitQuizAttemptReview(
            @Parameter(description = "ID of the quiz attempt") @PathVariable Long attemptId,
            @Parameter(description = "Review to submit") @RequestBody Object review) {
        quizAttemptService.submitQuizAttemptReview(attemptId, review);
        return ResponseEntity.ok().build();
    }
} 