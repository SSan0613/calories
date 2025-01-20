package burnCalories.diet.DTO.userDTO.userinfo;

import burnCalories.diet.domain.Gender;
import burnCalories.diet.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseUserInfoDTO {
    private String nickname;
    private String email;
    private double height;
    private double weight;
    private Gender gender;
    private int age;


    public ResponseUserInfoDTO(User user) {
        this.nickname = user.getNickname();
        this.email = user.getEmail();
        this.height = user.getHeight();
        this.weight = user.getWeight();
        this.gender = user.getGender();
        this.age = user.getAge();
    }


}
