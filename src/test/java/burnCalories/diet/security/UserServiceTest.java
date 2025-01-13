package burnCalories.diet.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;

import java.util.ArrayList;

@Service
public class UserServiceTest {

    @Autowired
    public UserRepository userRepository;

    @Test
    public void Test1() {
        User user = new User(1L,"test2","1234","abc","e@e",new ArrayList<>());

        userRepository.save(user);
    }
}
