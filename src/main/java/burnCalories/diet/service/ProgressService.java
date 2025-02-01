package burnCalories.diet.service;

import burnCalories.diet.DTO.challengeDTO.ResponseRankingDTO;
import burnCalories.diet.domain.Participants;
import burnCalories.diet.repository.ParticipantRepository;
import burnCalories.diet.repository.RecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressService {

    private final ParticipantRepository participantRepository;
    private final RecordRepository recordRepository;

    @Transactional
    public void updateChallengePercent(String username) {

        List<Participants> participantsList = participantRepository.findByUsername(username);

        for (Participants participants : participantsList) {
            double value = recordRepository.findbetweenByChallenge(participants.getChallenge());
            double percent = (value/participants.getChallenge().getGoalValue())*100;
            if(percent>=100){
                percent=100;
            }
            participants.updatePercent(percent,value);
        }
    }

    public List<ResponseRankingDTO> getChallengeRanking(Long id) {
        List<ResponseRankingDTO> rankingTop10 = participantRepository.findTop10ByOrderByPercent(id);
        long rank=1;
        for (ResponseRankingDTO responseRankingDTO : rankingTop10) {
            responseRankingDTO.setRank(rank++);
        }

        return rankingTop10;
/*
        Long userRanking = participantRepository.findUserRanking(username, id);

        Participants participants = participantRepository.findByUsernameAndChallengeId(username, id);
        if (participants == null) {
            return new ArrayList<>();
        }

        rankingTop10.add(new ResponseRankingDTO(participants.getUser().getNickname(),participants.getPercent(),participants.getMyValue(),userRanking));
*/


    }

    public ResponseRankingDTO getMyRanking(String username, Long id) {

        Long userRanking = participantRepository.findUserRanking(username, id);

        Participants participants = participantRepository.findByUsernameAndChallengeId(username, id);

        return new ResponseRankingDTO(participants.getUser().getNickname(),participants.getPercent(),participants.getMyValue(),userRanking);
    }
}
