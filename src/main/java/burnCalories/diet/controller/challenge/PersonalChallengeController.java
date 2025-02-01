package burnCalories.diet.controller.challenge;

import burnCalories.diet.DTO.challengeDTO.RequestChallengeDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseChallengeDetailsDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseChallengeListDTO;
import burnCalories.diet.DTO.challengeDTO.ResponseRankingDTO;
import burnCalories.diet.service.ProgressService;
import burnCalories.diet.service.challenge.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/challenges/personal")
public class PersonalChallengeController {

    private final ChallengeService challengeService;
    private final ProgressService progressService;

    //챌린지 목록 조회
    @GetMapping
    public List<ResponseChallengeListDTO> getChallengeAll(@RequestParam(required = false) String exerciseType) {
        List<ResponseChallengeListDTO> challengeList = challengeService.getChallengeList(exerciseType);

        return challengeList;
    }

    //관리자 챌린지 등록
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createNewChallenge(@AuthenticationPrincipal String username, @RequestBody RequestChallengeDTO requestChallengeDTO) {
        challengeService.createChallenge(requestChallengeDTO, username);
        return ResponseEntity.ok("챌린지 생성 완료");
    }

    //챌린지 상세 조회
    @GetMapping("/{id}")
    public ResponseEntity<ResponseChallengeDetailsDTO> getChallenge(@AuthenticationPrincipal String username, @PathVariable Long id) {

        ResponseChallengeDetailsDTO detailsDTO = challengeService.getChallenge(id, username);
        return ResponseEntity.ok(detailsDTO);
    }

    //관리자 챌린지 수정
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateChallenge(@AuthenticationPrincipal String username, @RequestBody RequestChallengeDTO requestChallengeDTO, @PathVariable Long id) {

        challengeService.updateChallenge(requestChallengeDTO, username, id);
        return ResponseEntity.ok("챌린지 수정 완료");
    }

    //관리자 챌린지 삭제
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteChallenge(@AuthenticationPrincipal String username, @PathVariable Long id) {
        challengeService.deleteChallenge(username, id);
        return ResponseEntity.ok("챌린지 삭제 완료");
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<String> joinChallenge(@AuthenticationPrincipal String username, @PathVariable Long id) {
        challengeService.register(username, id);
        return ResponseEntity.ok("챌린지 가입 완료");
    }

    @DeleteMapping("/{id}/out")
    public ResponseEntity<String> outChallenge(@AuthenticationPrincipal String username, @PathVariable Long id) {
        challengeService.outChallenge(username, id);
        return ResponseEntity.ok("챌린지 탈퇴 완료");
    }

    @GetMapping("{id}/rank")
    public List<ResponseRankingDTO> getChallengeRanking(@PathVariable Long id) {
        List<ResponseRankingDTO> rankingList;
        rankingList = progressService.getChallengeRanking(id);

        return rankingList;
    }

    @GetMapping("{id}/myrank")
    public ResponseRankingDTO getMyRanking(@AuthenticationPrincipal String username, @PathVariable Long id) {
        ResponseRankingDTO myRanking = progressService.getMyRanking(username, id);

        return myRanking;
    }
}
