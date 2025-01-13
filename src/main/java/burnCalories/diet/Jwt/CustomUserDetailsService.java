package burnCalories.diet.Jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("hello");
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found with" + username));
        log.info(""+ user.getUsername());
        return user;
    }
}
