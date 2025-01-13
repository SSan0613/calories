package burnCalories.diet.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import burnCalories.diet.DTO.securityDTO.SignUpDTO;
import burnCalories.diet.DTO.securityDTO.UpdateDTO;
import burnCalories.diet.Jwt.JwtToken;
import burnCalories.diet.Jwt.JwtTokenProvider;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    @Autowired
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public JwtToken signIn(String username, String password){

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("인증 객체 생성 완료");
        try {
            Authentication authenticate = authenticationManager.authenticate(authenticationToken);

            log.info("인증 완료");
            JwtToken jwtToken = jwtTokenProvider.generateToken(authenticate);
            log.info("jwt 토큰 생성 완료");
            return jwtToken;
        } catch (AuthenticationException e) {
            log.info("인증 실패");
            throw e;
        }
    }

    public void register(SignUpDTO signUpDTO) {
        if (!signUpDTO.getPassword().equals(signUpDTO.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호와 확인 비밀번호가 일치하지 않습니다.");
        }
        if (userRepository.existsByUsername(signUpDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }
        if (userRepository.existsByNickname(signUpDTO.getNickname())) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        if (signUpDTO.is_Teacher()) {
            roles.add("ADMIN");
        }
        User user = new User(signUpDTO.getUsername(), passwordEncoder.encode(signUpDTO.getPassword()), signUpDTO.getNickname(), signUpDTO.getEmail(), roles);
        userRepository.save(user);
    }

    public void update(UpdateDTO updateDTO, User findUser) {
        if (!updateDTO.getChangedPassword().equals(updateDTO.getChangedPasswordConfirm())) {
            throw new IllegalArgumentException("변경한 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

     //   Member findMember = memberRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        findUser.changePassword(passwordEncoder.encode(updateDTO.getChangedPassword()));
        userRepository.save(findUser);
    }

    public void delete(User user) {
        userRepository.delete(user);
    }
}
