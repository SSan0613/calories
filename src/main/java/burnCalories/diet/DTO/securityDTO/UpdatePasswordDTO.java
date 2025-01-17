package burnCalories.diet.DTO.securityDTO;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdatePasswordDTO {
    private String password;
    @Size(min = 4, max = 12)
    private String changedPassword;
    @Size(min = 4, max = 12)
    private String changedPasswordConfirm;
}
