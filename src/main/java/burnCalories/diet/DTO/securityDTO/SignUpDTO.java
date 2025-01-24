package burnCalories.diet.DTO.securityDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignUpDTO {
    @Size(min = 2, max = 10, message = "아이디는 2자 이상, 10자 이하여야 합니다")
    private String username;
    @NotNull(message = "비밀번호를 입력해 주세요")
    @Size(min = 4, max = 12, message = "비밀번호는 4자 이상, 12자 이하여야 합니다")
    private String password;
    @NotNull(message = "비밀번호 확인을 입력해 주세요")
    private String passwordConfirm;
    @NotNull(message = "닉네임을 입력해 주세요")
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상, 10자 이아혀야 합니다")
    private String nickname;
    @Email(message = "이메일 형식에 맞지 않습니다")
    private String email;
    private Boolean isManager;
    private String managerCode;

    public SignUpDTO(String username, String password, String passwordConfirm, String nickname, String email, Boolean isManager, String managerCode) {
        this.username = username;
        this.password = password;
        this.passwordConfirm = passwordConfirm;
        this.nickname = nickname;
        this.email = email;
        this.isManager = isManager;
        this.managerCode = managerCode;
    }
}
