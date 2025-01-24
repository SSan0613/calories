package burnCalories.diet.service.challenge;

import burnCalories.diet.DTO.challengeDTO.ResponseChallengeDetailsDTO;
import burnCalories.diet.domain.Challenge;
import burnCalories.diet.repository.ChallengeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PersonalChallengeServiceImplTest {

    @Autowired
    private ChallengeRepository challengeRepository;
    @Autowired
    private ChallengeService challengeService;

    @Test
    @DisplayName("챌린지 상세 조회")
    void test1() {

        Challenge challenge = challengeRepository.findById(2L).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 챌린지입니다"));

        System.out.println(challenge.getCreator().equals("홍길동"));
    }
}