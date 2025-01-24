package burnCalories.diet.service.challenge;

import burnCalories.diet.DTO.challengeDTO.RequestChallengeDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseChallengeDetailsDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseChallengeListDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ChallengeService {
    List<ResponseChallengeListDTO> getChallengeList(String exerciseType);

    void createChallenge(RequestChallengeDTO requestChallengeDTO, String username);

    void updateChallenge(RequestChallengeDTO requestChallengeDTO, String username, Long id);

    void deleteChallenge(String username, Long id);

    ResponseChallengeDetailsDTO getChallenge(Long id,String username);

    void register(String username, Long id);

    void outChallenge(String username, Long id);
}
