package burnCalories.diet.controller;

import burnCalories.diet.DTO.userDTO.userinfo.ResponseUserInfoDTO;
import burnCalories.diet.DTO.userDTO.userinfo.UpdateUserInfoDTO;
import burnCalories.diet.DTO.userDTO.userinfo.changeNicknameDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import burnCalories.diet.repository.UserRepository;
import burnCalories.diet.service.UserService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    //테스트
    @GetMapping("/debug")
    public ResponseEntity<Object> debugAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok(authentication);
    }
    //닉네임 수정
    @PutMapping("/nickname")
    public ResponseEntity<String> changeNickname(@AuthenticationPrincipal String username, @RequestBody changeNicknameDTO nickname) {
        userService.changeNickname(username,nickname);
        return ResponseEntity.ok("닉네임 변경 완료");
    }
    //마이페이지 정보 호출
    @GetMapping("/info")
    public ResponseEntity<ResponseUserInfoDTO> getInfo(@AuthenticationPrincipal String username) {
        ResponseUserInfoDTO info = userService.getInfo(username);

        return ResponseEntity.ok(info);
    }

    //신체 정보 수정
    @PutMapping("/info")
    public ResponseEntity<String> changeInfo(@AuthenticationPrincipal String username, @RequestBody @Valid UpdateUserInfoDTO updateUserInfoDTO) {

        userService.changeInfo(username, updateUserInfoDTO);

        return ResponseEntity.ok("회원정보 수정 완료");
    }



}
