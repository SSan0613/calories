package burnCalories.diet.service.challenge;

import burnCalories.diet.DTO.challengeDTO.RequestChallengeDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseChallengeDetailsDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseChallengeListDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseRankingDTO;
import burnCalories.diet.domain.Challenge;
import burnCalories.diet.domain.Participants;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.ChallengeRepository;
import burnCalories.diet.repository.ParticipantRepository;
import burnCalories.diet.repository.RecordRepository;
import burnCalories.diet.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalChallengeServiceImpl implements ChallengeService{

    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;
    private final RecordRepository recordRepository;

    public List<ResponseChallengeListDTO> getChallengeList(String exerciseType){

        List<ResponseChallengeListDTO> challengeList = new ArrayList<>();
        if (exerciseType == null) {
            challengeList   = challengeRepository.findChallengeListAll();
        }else{
            challengeList = challengeRepository.findChallengeListByExerciseType(exerciseType);
        }
        return challengeList;
    }

    @Override
    public void createChallenge(RequestChallengeDTO requestChallengeDTO, String username) {

        User findUser = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));
        Challenge newChallenge = new Challenge(requestChallengeDTO, findUser);

        challengeRepository.save(newChallenge);

    }

    @Override
    @Transactional
    public void updateChallenge(RequestChallengeDTO requestChallengeDTO, String username, Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 챌린지입니다"));
        if(!challenge.getCreator().getUsername().equals(username)){
            throw new IllegalArgumentException("잘못된 접근입니다");
        }
        challenge.update(requestChallengeDTO);
    }

    @Override
    public void deleteChallenge(String username, Long id) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 챌린지입니다"));
        if(!challenge.getCreator().getUsername().equals(username)){
            throw new IllegalArgumentException("잘못된 접근입니다");
        }
        challengeRepository.delete(challenge);
    }

    @Override
    public ResponseChallengeDetailsDTO getChallenge(Long id,String username) {
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 챌린지입니다"));
        double myValue = 0;
        boolean isComplete = false;
        double percent = 0;
        Long count = participantRepository.countByChallenge(challenge);

        boolean isParticipant = participantRepository.existsByUsername(challenge,username);
        if(isParticipant){
            /*myValue = recordRepository.findbetweenByChallenge(challenge);
            percent = (myValue/ challenge.getGoalValue())*100;*/
            Participants participants = participantRepository.findByUsernameAndChallengeId(username, challenge.getChallengeId());
            myValue= participants.getMyValue();
            percent = participants.getPercent();
        }
        if(percent>=100){
            isComplete=true;
        }

        return new ResponseChallengeDetailsDTO(challenge,count,isParticipant,myValue,isComplete,percent);
    }

    @Override
    public void register(String username, Long id) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        Challenge challenge = challengeRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 챌린지입니다"));

        participantRepository.save(new Participants(user, challenge, LocalDateTime.now()));
    }

    @Override
    public void outChallenge(String username, Long id) {

        Participants memberParticipant = participantRepository.findByUsernameAndChallengeId(username, id);

        participantRepository.delete(memberParticipant);
    }

}
