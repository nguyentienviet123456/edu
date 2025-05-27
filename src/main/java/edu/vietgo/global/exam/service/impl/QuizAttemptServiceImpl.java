package edu.vietgo.global.exam.service.impl;

import edu.vietgo.global.exam.dto.UserAnswerDTO;
import edu.vietgo.global.exam.entity.*;
import edu.vietgo.global.exam.exception.QuizAttemptException;
import edu.vietgo.global.exam.repository.*;
import edu.vietgo.global.exam.service.QuizAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class QuizAttemptServiceImpl implements QuizAttemptService {

    public static final double DEFAULT_SCORE = 0.0;
    private final QuizAttemptRepository quizAttemptRepository;
    private final QuizRepository quizRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    // Cache to store temporary answers
    private final Map<Long, Map<Long, UserAnswerDTO>> temporaryAnswersCache = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public QuizAttempt startQuizAttempt(Long quizId, Long userId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizAttemptException("Quiz not found"));

        // Check if user has any ongoing attempts
        List<QuizAttempt> ongoingAttempts = quizAttemptRepository.findByQuizIdAndUserIdAndStatus(
                quizId, userId, QuizAttemptStatus.IN_PROGRESS);
        if (!ongoingAttempts.isEmpty()) {
            throw new QuizAttemptException("User has an ongoing attempt for this quiz");
        }

        QuizAttempt attempt = new QuizAttempt();
        attempt.setQuiz(quiz);
        attempt.setUserId(userId);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setStatus(QuizAttemptStatus.IN_PROGRESS);
        attempt.setScore(DEFAULT_SCORE);

        return quizAttemptRepository.save(attempt);
    }

    @Override
    @Transactional
    public QuizAttempt submitQuizAttempt(Long attemptId) {
        QuizAttempt attempt = quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new QuizAttemptException("Quiz attempt not found"));

        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            throw new QuizAttemptException("Quiz attempt is not in progress");
        }

        // Calculate total score
        List<UserAnswer> userAnswers = userAnswerRepository.findByQuizAttempt(attempt);
        double totalScore = userAnswers.stream()
                .mapToDouble(UserAnswer::getPoints)
                .sum();

        attempt.setEndTime(LocalDateTime.now());
        attempt.setStatus(QuizAttemptStatus.SUBMITTED);
        attempt.setScore(totalScore);

        return quizAttemptRepository.save(attempt);
    }

    @Override
    public QuizAttempt getQuizAttempt(Long attemptId) {
        return quizAttemptRepository.findById(attemptId)
                .orElseThrow(() -> new QuizAttemptException("Quiz attempt not found"));
    }

    @Override
    public Long getRemainingTime(Long attemptId) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            return 0L;
        }

        LocalDateTime startTime = attempt.getStartTime();
        Integer duration = attempt.getQuiz().getDuration();
        LocalDateTime endTime = startTime.plusMinutes(duration);
        LocalDateTime now = LocalDateTime.now();

        if (now.isAfter(endTime)) {
            return 0L;
        }

        return ChronoUnit.SECONDS.between(now, endTime);
    }

    @Override
    @Transactional
    public QuizAttempt extendTime(Long attemptId, Integer additionalMinutes) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            throw new QuizAttemptException("Cannot extend time for non-in-progress attempt");
        }

        // Add additional time to quiz duration
        Quiz quiz = attempt.getQuiz();
        quiz.setDuration(quiz.getDuration() + additionalMinutes);
        quizRepository.save(quiz);

        return attempt;
    }

    @Transactional
    public void saveUserAnswer(Long attemptId, Long questionId, Object answer) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            throw new QuizAttemptException("Cannot save answer for non-in-progress attempt");
        }

        // Check if answer already exists
        if (userAnswerRepository.existsByQuizAttemptIdAndQuestionId(attemptId, questionId)) {
            throw new QuizAttemptException("Answer already exists for this question");
        }

        // Create and save user answer
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setQuizAttempt(attempt);
        // Set other answer details based on answer type
        // This will be implemented based on the answer format

        userAnswerRepository.save(userAnswer);
    }

    @Transactional
    public void updateUserAnswer(Long attemptId, Long questionId, Object answer) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            throw new QuizAttemptException("Cannot update answer for non-in-progress attempt");
        }

        UserAnswer userAnswer = userAnswerRepository.findByQuizAttemptIdAndQuestionId(attemptId, questionId);
        if (userAnswer == null) {
            throw new QuizAttemptException("Answer not found for this question");
        }

        // Update answer details based on answer type
        // This will be implemented based on the answer format

        userAnswerRepository.save(userAnswer);
    }

//    @Override
//    public List<?> getUserAnswers(Long attemptId) {
//        QuizAttempt attempt = getQuizAttempt(attemptId);
//        return userAnswerRepository.findByQuizAttempt(attempt);
//    }

    @Override
    public Object getQuizAttemptResult(Long attemptId) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.SUBMITTED) {
            throw new QuizAttemptException("Quiz attempt is not submitted");
        }

        // Create result object with all necessary details
        Map<String, Object> result = Map.of(
            "attemptId", attempt.getId(),
            "quizId", attempt.getQuiz().getId(),
            "userId", attempt.getUserId(),
            "score", attempt.getScore(),
            "startTime", attempt.getStartTime(),
            "endTime", attempt.getEndTime(),
            "answers", getUserAnswers(attemptId)
        );

        return result;
    }

    @Override
    public Object getQuizStatistics(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizAttemptException("Quiz not found"));

        Double averageScore = getAverageScore(quizId);
        Double highestScore = getHighestScore(quizId);
        long totalAttempts = quizAttemptRepository.findByQuiz(quiz).size();
        long passedAttempts = quizAttemptRepository.findByQuizAndStatus(quiz, QuizAttemptStatus.SUBMITTED)
                .stream()
                .filter(attempt -> attempt.getScore() >= quiz.getPassingScore())
                .count();

        return Map.of(
            "quizId", quizId,
            "totalAttempts", totalAttempts,
            "passedAttempts", passedAttempts,
            "passRate", totalAttempts > 0 ? (double) passedAttempts / totalAttempts : 0,
            "averageScore", averageScore,
            "highestScore", highestScore
        );
    }

    @Override
    public Page<QuizAttempt> getUserQuizHistory(Long userId, Pageable pageable) {
        return quizAttemptRepository.findByUserId(userId, pageable);
    }

    @Override
    public List<QuizAttempt> getQuizAttemptsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return quizAttemptRepository.findByDateRange(startDate, endDate);
    }

    @Override
    public List<QuizAttempt> getQuizAttemptsByScoreRange(Double minScore, Double maxScore) {
        return quizAttemptRepository.findByScoreRange(minScore, maxScore);
    }

    @Override
    public List<QuizAttempt> getQuizAttemptsByStatus(QuizAttemptStatus status) {
        return quizAttemptRepository.findByStatus(status);
    }

    @Override
    public List<QuizAttempt> getQuizAttemptsByQuizAndUser(Quiz quiz, Long userId) {
        return quizAttemptRepository.findByQuizAndUserId(quiz, userId);
    }

    @Override
    public List<QuizAttempt> getQuizAttemptsByQuizAndStatus(Quiz quiz, QuizAttemptStatus status) {
        return quizAttemptRepository.findByQuizAndStatus(quiz, status);
    }

    @Override
    public Double getAverageScore(Long quizId) {
        return quizAttemptRepository.getAverageScoreByQuizId(quizId);
    }

    @Override
    public Double getHighestScore(Long quizId) {
        return quizAttemptRepository.getHighestScoreByQuizId(quizId);
    }

    @Override
    public Object getQuizAttemptReview(Long attemptId) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.SUBMITTED) {
            throw new QuizAttemptException("Cannot review non-submitted attempt");
        }

        // Create review object with all necessary details
        return Map.of(
            "attemptId", attempt.getId(),
            "quizId", attempt.getQuiz().getId(),
            "userId", attempt.getUserId(),
            "score", attempt.getScore(),
            "startTime", attempt.getStartTime(),
            "endTime", attempt.getEndTime(),
            "answers", getUserAnswers(attemptId),
            "correctAnswers", userAnswerRepository.findByQuizAttemptAndIsCorrectTrue(attempt)
        );
    }

    @Override
    @Transactional
    public void submitQuizAttemptReview(Long attemptId, Object review) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.SUBMITTED) {
            throw new QuizAttemptException("Cannot submit review for non-submitted attempt");
        }

        // Process and save review
        // This will be implemented based on the review format
    }

    @Override
    public void saveTemporaryAnswer(Long attemptId, UserAnswerDTO answer) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            throw new QuizAttemptException("Cannot save answer for non-in-progress attempt");
        }

        // Get or create cache for this attempt
        Map<Long, UserAnswerDTO> attemptCache = temporaryAnswersCache.computeIfAbsent(attemptId, k -> new HashMap<>());
        
        // Save answer to cache
        attemptCache.put(answer.getQuestionId(), answer);
        
        // Auto-save to database every 5 answers
        if (attemptCache.size() % 5 == 0) {
            saveAllAnswers(attemptId);
        }
    }

    @Override
    public Map<Long, UserAnswerDTO> getTemporaryAnswers(Long attemptId) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            throw new QuizAttemptException("Cannot get temporary answers for non-in-progress attempt");
        }

        return temporaryAnswersCache.getOrDefault(attemptId, new HashMap<>());
    }

    @Override
    @Transactional
    public void saveAllAnswers(Long attemptId) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        
        if (attempt.getStatus() != QuizAttemptStatus.IN_PROGRESS) {
            throw new QuizAttemptException("Cannot save answers for non-in-progress attempt");
        }

        Map<Long, UserAnswerDTO> attemptCache = temporaryAnswersCache.get(attemptId);
        if (attemptCache == null || attemptCache.isEmpty()) {
            return;
        }

        // Convert DTOs to entities and save to database
        for (UserAnswerDTO answerDTO : attemptCache.values()) {
            Question question = questionRepository.findById(answerDTO.getQuestionId())
                    .orElseThrow(() -> new QuizAttemptException("Question not found"));

            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setQuizAttempt(attempt);
            userAnswer.setQuestion(question);
            
            if (answerDTO.getAnswerId() != null) {
                Answer answer = answerRepository.findById(answerDTO.getAnswerId())
                        .orElseThrow(() -> new QuizAttemptException("Answer not found"));
                userAnswer.setAnswer(answer);
            }
            
            userAnswer.setTextAnswer(answerDTO.getTextAnswer());
            userAnswer.setIsCorrect(answerDTO.getIsCorrect());
            userAnswer.setPoints(answerDTO.getPoints());

            userAnswerRepository.save(userAnswer);
        }

        // Clear cache after saving
        temporaryAnswersCache.remove(attemptId);
    }

    @Override
    public List<UserAnswerDTO> getUserAnswers(Long attemptId) {
        QuizAttempt attempt = getQuizAttempt(attemptId);
        List<UserAnswer> userAnswers = userAnswerRepository.findByQuizAttempt(attempt);
        
        return userAnswers.stream()
                .map(this::convertToDTO)
                .toList();
    }

    private UserAnswerDTO convertToDTO(UserAnswer userAnswer) {
        UserAnswerDTO dto = new UserAnswerDTO();
        dto.setQuestionId(userAnswer.getQuestion().getId());
        if (userAnswer.getAnswer() != null) {
            dto.setAnswerId(userAnswer.getAnswer().getId());
        }
        dto.setTextAnswer(userAnswer.getTextAnswer());
        dto.setIsCorrect(userAnswer.getIsCorrect());
        dto.setPoints(userAnswer.getPoints());
        return dto;
    }
} 