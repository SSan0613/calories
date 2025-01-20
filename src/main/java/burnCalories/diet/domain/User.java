package burnCalories.diet.domain;

import burnCalories.diet.DTO.userDTO.userinfo.UpdateUserInfoDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "members")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String nickname;
    @Column
    private String email;
    @Column
    private double height;
    @Column
    private double weight;
    @Column
    private Gender gender;
    @Column
    private int age;
    @Column
    private int is_manager;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "members_roles")
    private List<String> roles = new ArrayList<>();

    public User(String username, String password, String nickname, String email, List<String> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.roles = roles;
    }

    public void changeInfo(UpdateUserInfoDTO updateUserInfoDTO) {
        this.height = updateUserInfoDTO.getHeight();;
        this.weight = updateUserInfoDTO.getWeight();
        this.age = updateUserInfoDTO.getAge();
        this.gender = updateUserInfoDTO.getGender();
    }
    public void changePassword(String password) {
        this.password = password;
    }
    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

/*
    public void changeHeight(double height){ this.height = height;}
    public void changeWeight(double weight) { this.weight = weight;}
*/

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }



}
