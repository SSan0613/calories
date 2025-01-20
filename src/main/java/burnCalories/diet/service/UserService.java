package burnCalories.diet.service;

import burnCalories.diet.DTO.userDTO.exerciseLog.ResponseCaloriesLogDTO;
import burnCalories.diet.DTO.userDTO.exerciseLog.ResponseDurationLogDTO;
import burnCalories.diet.DTO.userDTO.exerciseLog.ResponseTodayLogDTO;
import burnCalories.diet.DTO.userDTO.userinfo.ResponseUserInfoDTO;
import burnCalories.diet.DTO.userDTO.userinfo.UpdateUserInfoDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import burnCalories.diet.DTO.userDTO.exerciseLog.ExerciseLogDTO;
import burnCalories.diet.domain.User;
import burnCalories.diet.domain.Records;
import burnCalories.diet.repository.RecordRepository;
import burnCalories.diet.repository.UserRepository;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    public ResponseUserInfoDTO getInfo(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        return new ResponseUserInfoDTO(user);
    }

    @Transactional
    public void changeNickname(String username, String nickname) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        user.changeNickname(nickname);
    }

    @Transactional
    public void changeInfo(String username, UpdateUserInfoDTO updateUserInfoDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        user.changeInfo(updateUserInfoDTO);

/*
        String changedNickname = updateUserInfoDTO.getNickname();

        double changedHeight = updateUserInfoDTO.getHeight();
        double chandgedWeight = updateUserInfoDTO.getWeight();

        if(!user.getNickname().equals(updateUserInfoDTO.getNickname())) user.changeNickname(changedNickname);
        if(user.getHeight()!=changedHeight) user.changeHeight(changedHeight);
        if(user.getWeight()!=chandgedWeight) user.changeWeight(chandgedWeight);
*/
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
        //머신러닝 모델에서 예상 칼로리 반환
        double calories = requestCalories(durationDouble,exerciseType);

        User user = userRepository.findByUsername(username).get();

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

    private double requestCalories(double duration, String exerciseType) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("exerciseType", exerciseType);
        requestBody.put("duration", duration);

        try {
            return WebClient.builder()
                    .baseUrl("http://localhost:5000")
                    .build().post()
                    .uri("/predict")
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

    public List<ResponseTodayLogDTO> getTodayExerciseLog() {
        LocalDateTime dateTime = LocalDateTime.now();

        LocalDateTime start = LocalDateTime.now().with(LocalTime.MIN);
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);
        log.info(String.valueOf(start));
        log.info(String.valueOf(end));
        return recordRepository.findRecordsByDateTime(start,end,dateTime);

    }
    @Transactional
    public void changeExerciseLog(Long id, ExerciseLogDTO exerciseLogDTO) {
        Records findRecord = recordRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        double duration = calculateDuration(exerciseLogDTO);
        double calories = requestCalories(duration, exerciseLogDTO.getExerciseType());
        findRecord.updateExerciseLog(exerciseLogDTO,duration,calories);
        //recordRepository.save(findRecord);
    }

    public void deleteExerciseLog(Long id) {
        recordRepository.deleteById(id);
    }



}
