package burnCalories.diet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import burnCalories.diet.DTO.userDTO.ExerciseLogDTO;
import burnCalories.diet.domain.User;
import burnCalories.diet.domain.Records;
import burnCalories.diet.repository.RecordRepository;
import burnCalories.diet.repository.UserRepository;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;

    public void changeInfo(String username, String nickname) {
        User user = userRepository.findByUsername(username).get();
        user.changeNickname(nickname);
        userRepository.save(user);
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
        LocalDateTime endDateTime = exerciseLog.getEndTime();
        LocalDateTime startDateTime = exerciseLog.getStartTime();

        Duration duration = Duration.between(startDateTime, endDateTime);

        float durationFloat = (float) (duration.toMinutes() / 60.0);
        String exerciseType = exerciseLog.getExerciseType();

        double calories = calculateCalories(duration,exerciseType);
        User user = userRepository.findByUsername(username).get();
        Records records = new Records(user, exerciseLog.getStartTime(), exerciseLog.getEndTime(), exerciseType, durationFloat, calories);
        recordRepository.save(records);

    }

    private double calculateCalories(Duration duration, String exerciseType) {

        return 10.0;
    }
}
