package burnCalories.diet.service;

import burnCalories.diet.DTO.userDTO.userinfo.ResponseUserInfoDTO;
import burnCalories.diet.DTO.userDTO.userinfo.UpdateUserInfoDTO;
import burnCalories.diet.DTO.userDTO.userinfo.changeNicknameDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import burnCalories.diet.domain.User;
import burnCalories.diet.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;


    public ResponseUserInfoDTO getInfo(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));
        return new ResponseUserInfoDTO(user);
    }

    @Transactional
    public void changeNickname(String username, changeNicknameDTO nickname) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
        user.changeNickname(nickname);

        //userRepository.save(user);
    }

    @Transactional
    public void changeInfo(String username, UpdateUserInfoDTO updateUserInfoDTO) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다"));

        user.changeInfo(updateUserInfoDTO);

/*
        String changedNickname = updateUserInfoDTO.getNickname();

        double changedHeight = updateUserInfoDTO.getHeight();
        double chandgedWeight = updateUserInfoDTO.getWeight();

        if(!user.getNickname().equals(updateUserInfoDTO.getNickname())) user.changeNickname(changedNickname);
        if(user.getHeight()!=changedHeight) user.changeHeight(changedHeight);
        if(user.getWeight()!=chandgedWeight) user.changeWeight(chandgedWeight);
*/
    }


}
