package burnCalories.diet.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "records")
@Getter
@NoArgsConstructor
public class Records {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId")
    private User user;
    @Column
    private LocalDateTime startTime;
    @Column
    private LocalDateTime endTime;
    @Column
    private String exerciseType;
    @Column
    private float duration;
    @Column
    private double calories;

    public Records(User user, LocalDateTime startTime, LocalDateTime endTime, String exerciseType, float duration, double calories) {
        this.user = user;
        this.startTime = startTime;
        this.endTime = endTime;
        this.exerciseType = exerciseType;
        this.duration = duration;
        this.calories = calories;
    }
}