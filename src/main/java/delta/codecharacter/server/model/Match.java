package delta.codecharacter.server.model;

import delta.codecharacter.server.util.enums.MatchMode;
import delta.codecharacter.server.util.enums.Status;
import delta.codecharacter.server.util.enums.Verdict;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * A match between two players. It may consist of more than one game.
 */
@Data
@Builder
public class Match {
    @Id
    @NotNull
    @Positive
    private Integer id;

    @Field("player_id_1")
    @NotNull
    @Positive
    private Integer playerId1;

    @Field("player_id_2")
    @NotNull
    @Positive
    private Integer playerId2;

    @NotNull
    @Builder.Default
    private Verdict verdict = Verdict.TIE;

    @NotNull
    @Builder.Default
    private Status status = Status.IDLE;

    @Field("score_1")
    @Builder.Default
    private Integer score1 = 0;

    @Field("score_2")
    @Builder.Default
    private Integer score2 = 0;

    @Field("match_mode")
    @NotNull
    @Builder.Default
    private MatchMode matchMode = MatchMode.AUTO;

    @Field("created_at")
    @NotNull
    @Builder.Default
    private Date createdAt = new Date();
}
