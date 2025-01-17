package burnCalories.diet.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.ArrayList;

@Service
public class UserServiceTest {

    @Autowired
    public UserRepository userRepository;

    @Test
    @DisplayName("날짜 테스트")
    public void dayTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        int today = dateTime.get(ChronoField.DAY_OF_WEEK);
        System.out.printf("오늘은 일주일중의 %d 일입니다\n", today);
        LocalDateTime start_week_Day = dateTime.minusDays(today-1);
        LocalDateTime end_week_Day = start_week_Day.plusDays(6);
        System.out.println(start_week_Day + " \n" + end_week_Day);
    }
}
