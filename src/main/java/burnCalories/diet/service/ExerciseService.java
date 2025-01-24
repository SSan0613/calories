package burnCalories.diet.service;

import burnCalories.diet.DTO.exerciseDTO.*;
import burnCalories.diet.domain.Records;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.RecordRepository;
import burnCalories.diet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    public List<ResponseTodayLogDTO> getTodayExerciseLog() {
        LocalDateTime dateTime = LocalDateTime.now();

        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        log.info(String.valueOf(start));
        log.info(String.valueOf(end));
        return recordRepository.findRecordsByDateTime(start,end,dateTime);

    }

    public List<ResponseDurationLogDTO> getDurationLog(int weeks, String username) {
        System.out.println(username);
        LocalDateTime dateTime = LocalDateTime.now();

        int today = dateTime.get(ChronoField.DAY_OF_WEEK);
        LocalDateTime start_week_Day = dateTime.minusDays(today-1 - weeks*7).with(LocalTime.MIN);
        LocalDateTime end_week_Day = start_week_Day.plusDays(6).with(LocalTime.MAX);
        log.info(String.valueOf(start_week_Day));
        log.info(String.valueOf(end_week_Day));

        List<ResponseDurationLogDTO> durationLogs = recordRepository.findDurationBetweenStartEnd(start_week_Day, end_week_Day, username);

        return durationLogs;
    }

    public List<ResponseCaloriesLogDTO>  getCaloriesLog(int weeks, String username) {
        LocalDateTime dateTime = LocalDateTime.now();
        int today = dateTime.get(ChronoField.DAY_OF_WEEK);
        LocalDateTime start_week_Day = dateTime.minusDays(today-1 - weeks*7).with(LocalTime.MIN);
        LocalDateTime end_week_Day = start_week_Day.plusDays(6).with(LocalTime.MAX);
        log.info(String.valueOf(start_week_Day));
        log.info(String.valueOf(end_week_Day));

        List<ResponseCaloriesLogDTO> caloriesLogs = recordRepository.findCaloriesBetweenStartEnd(start_week_Day, end_week_Day, username);

        return caloriesLogs;
    }
    @Transactional
    public void changeExerciseLog(String username,Long id, ExerciseLogDTO exerciseLogDTO) {
        Records findRecord = recordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        double duration = calculateDuration(exerciseLogDTO);
        double calories = requestCalories(duration, exerciseLogDTO.getExerciseType(),user);
        findRecord.updateExerciseLog(exerciseLogDTO,duration,calories);
        //recordRepository.save(findRecord);
    }

    public void deleteExerciseLog(Long id) {
        recordRepository.deleteById(id);
    }


    public void validateDate(ExerciseLogDTO exerciseLog) {
        LocalDateTime startTime = exerciseLog.getStartTime();
        LocalDateTime endTime = exerciseLog.getEndTime();

        LocalDateTime yesterdayMin = LocalDateTime.now().minusDays(1).with(LocalTime.MIN);
        LocalDateTime tomorrowMin = LocalDateTime.now().plusDays(1).with(LocalTime.MIN);
        if (endTime.isBefore(startTime)) {
            throw new IllegalArgumentException("시작시간이 종료시간보다 빠를 수 없습니다");
        }
        if (startTime.isBefore(yesterdayMin) || startTime.isAfter(tomorrowMin)) {
            throw new IllegalArgumentException("시작시간은 최대 전일, 당일까지 가능합니다");
        }

    }

    public void putExerciseLog(String username, ExerciseLogDTO exerciseLog) {
        double durationDouble = calculateDuration(exerciseLog);
        String exerciseType = exerciseLog.getExerciseType();

        log.info(String.valueOf(exerciseLog.getStartTime()));
        log.info(String.valueOf(exerciseLog.getEndTime()));

        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        //머신러닝 모델에서 예상 칼로리 반환
        double calories = requestCalories(durationDouble,exerciseType,user);



        Records records = new Records(user, exerciseLog.getStartTime(), exerciseLog.getEndTime(), exerciseType, durationDouble, calories);

        recordRepository.save(records);

    }

    private static double calculateDuration(ExerciseLogDTO exerciseLog) {
        LocalDateTime endDateTime = exerciseLog.getEndTime();
        LocalDateTime startDateTime = exerciseLog.getStartTime();

        Duration duration = Duration.between(startDateTime, endDateTime);

        double durationDouble = (duration.toMinutes() / 60.0);
        return durationDouble;
    }

    private double requestCalories(double duration, String exerciseType,User user) {
        Map<String, Object> requestBody = new HashMap<>();
        Map<String,Integer> workoutTypeMap = getWorkoutTypeMap();
        int gender=0;
        int findExerciseType = workoutTypeMap.get(exerciseType);
        log.info("request exercisetype : " + findExerciseType);
        requestBody.put("workout_type", findExerciseType);
        requestBody.put("session_duration", duration);
        requestBody.put("age",user.getAge());
        if(user.getGender().equals("Female")) gender=1;
        requestBody.put("gender",gender);
        requestBody.put("weight",user.getWeight());
        requestBody.put("height",user.getHeight());

        try {
            return WebClient.builder()
                    .baseUrl("http://localhost:8000")
                    .build().post()
                    .uri("/predict-calories")
                    .header("Content-Type","application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(Double.class)
                    .timeout(Duration.ofSeconds(8)) //8초 타임아웃
                    .block();
        } catch (Exception e) {
            log.info("ML model request or response error");
            log.error(e.getMessage());
            throw new IllegalArgumentException("failed to load model");
        }
    }

    private Map<String, Integer> getWorkoutTypeMap() {
        Map<String,Integer> map = Map.of(
                "CARDIO", 0,
                "HIIT", 1,
                "STRENGTH", 2,
                "YOGA", 3
        );
        return map;
    }


    public ResponseRecommendDTO recommendExercise(String username, double duration) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        Map<String, Object> requestBody = new HashMap<>();
        int gender = 0;

        requestBody.put("session_duration", duration);
        requestBody.put("age",user.getAge());
        if(user.getGender().equals("Female")) gender=1;
        requestBody.put("gender",gender);
        requestBody.put("weight",user.getWeight());
        requestBody.put("height",user.getHeight());

        try {
            Map<String, Object> response = WebClient.builder()
                    .baseUrl("http://localhost:8000")
                    .build().post()
                    .uri("/recommend-workout")
                    .header("Content-Type", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                    })
                    .timeout(Duration.ofSeconds(8)) //8초 타임아웃
                    .block();
            String recommendedWorkout = (String) response.get("recommended_workout");
            double expectedCaloriesBurned = ((Number) response.get("expected_calories_burned")).doubleValue();

            return new ResponseRecommendDTO(recommendedWorkout,expectedCaloriesBurned,duration);

        } catch (Exception e) {
            log.info("ML model request or response error");
            log.error(e.getMessage());
            throw new IllegalArgumentException("failed to load model");
        }
    }
}
