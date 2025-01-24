package burnCalories.diet.service;

import burnCalories.diet.DTO.securityDTO.SignUpDTO;
import burnCalories.diet.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Value("${manager.code}")
    String adminSignupCode;

    @Test
    @DisplayName(("관리자 코드 확인"))
    public void test1() {
        Assertions.assertEquals(adminSignupCode,"helloManager");
    }

    @Test
    @DisplayName("관리자 회원가입 테스트")
    public void test2() {
        SignUpDTO signUpDTO = new SignUpDTO("test1901", "1234", "1234",
                "fjalds", "test@test.com", true, "helloManager");

        List<String> roles = new ArrayList<>();
        roles.add("ROLE_USER");
        if (signUpDTO.getIsManager()) {
            if(signUpDTO.getManagerCode()==null ||!signUpDTO.getManagerCode().equals(adminSignupCode)){
                throw new IllegalArgumentException("관리자 코드 오류");
            }
            roles.add("ROLE_ADMIN");
        }
        Assertions.assertEquals(roles.get(0),"ROLE_USER");
        Assertions.assertEquals(roles.get(1),"ROLE_ADMIN");
    }
}