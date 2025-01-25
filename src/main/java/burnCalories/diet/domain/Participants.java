package burnCalories.diet.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "participants")
@NoArgsConstructor
@Getter
public class Participants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challengeId")
    private Challenge challenge;

    @Column
    private LocalDateTime completedAt;

    @Column
    private double myValue;

    @Column
    private double percent;

    @Column
    private LocalDateTime joinedAt;

    public Participants(User user, Challenge challenge,LocalDateTime joinedAt) {
        this.user = user;
        this.challenge = challenge;
        this.joinedAt = joinedAt;
    }

    public void updatePercent(double percent,double value) {
        this.percent = percent;
        this.myValue = value;
    }
}
