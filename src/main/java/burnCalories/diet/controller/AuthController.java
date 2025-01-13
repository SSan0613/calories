package burnCalories.diet.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import burnCalories.diet.DTO.securityDTO.LoginDTO;
import burnCalories.diet.DTO.securityDTO.SignUpDTO;
import burnCalories.diet.DTO.securityDTO.UpdateDTO;
import burnCalories.diet.Jwt.JwtToken;
import burnCalories.diet.domain.User;
import burnCalories.diet.service.AuthService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService memberService;

    @PostMapping("/login")
    public JwtToken login(@RequestBody LoginDTO loginDTO) {
        JwtToken jwtToken = memberService.signIn(loginDTO.getUsername(), loginDTO.getPassword());

        return jwtToken;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignUpDTO signUpDTO) {
        try {
            memberService.register(signUpDTO);
            return ResponseEntity.ok("회원가입이 완료되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/changePass")
    public ResponseEntity<String> updatePassword(@Valid @RequestBody UpdateDTO updateDTO) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberService.update(updateDTO,principal);
        return ResponseEntity.ok("비밀번호를 변경하였습니다.");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteUser() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberService.delete(user);
        return ResponseEntity.ok("회원탈퇴가 완료되었습니다");
    }

    @GetMapping("/test")
    public String test() {

        return "hello";
    }
}
