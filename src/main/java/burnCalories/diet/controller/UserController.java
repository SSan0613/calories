package burnCalories.diet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import burnCalories.diet.DTO.userDTO.ExerciseLogDTO;
import burnCalories.diet.DTO.userDTO.changeInfoDTO;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;
import burnCalories.diet.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping("/debug")
    public ResponseEntity<Object> debugAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication);
    }
    @GetMapping("/info")
    public ResponseEntity<User> getInfo(@AuthenticationPrincipal String username) {
        User user = userRepository.findByUsername(username).get();

        log.info(user.getUsername(), user.getNickname());
        return ResponseEntity.ok(user);
    }

    @PutMapping("/info")
    public ResponseEntity<String> changeInfo(@AuthenticationPrincipal String username,@RequestBody @Valid changeInfoDTO changeInfoDTO) {
        String nickname = changeInfoDTO.getNickname();
        userService.changeInfo(username, nickname);

        return ResponseEntity.ok("회원정보 수정 완료");
    }


    @PostMapping("/log")
    public ResponseEntity<String> putExerciseLog(@AuthenticationPrincipal String username, @RequestBody @Valid ExerciseLogDTO exerciseLog) {
        try {
            userService.validateDate(exerciseLog);

            userService.putExerciseLog(username, exerciseLog);
            return ResponseEntity.ok("운동 기록 완료");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }




    }
}
