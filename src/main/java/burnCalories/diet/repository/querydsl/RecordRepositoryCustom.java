package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.exerciseDTO.ResponseCaloriesLogDTO;
import burnCalories.diet.DTO.exerciseDTO.ResponseDurationLogDTO;
import burnCalories.diet.DTO.exerciseDTO.ResponseTodayLogDTO;
import burnCalories.diet.domain.Challenge;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepositoryCustom {
    List<ResponseDurationLogDTO> findDurationBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username);
    List<ResponseCaloriesLogDTO> findCaloriesBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username);

    List<ResponseTodayLogDTO> findRecordsByUsernameAndDateTime(String username, LocalDateTime start, LocalDateTime end, LocalDateTime dateTime);

    double findbetweenByChallenge(Challenge challenge);
}
