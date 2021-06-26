package com.kosticnikola.transfer.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@NoArgsConstructor
@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Getter
    @Setter
    private Long id;

    @Column(name = "created_at")
    @Getter
    @Setter
    private Timestamp createdAt;

    @Column(name = "player_id")
    @Getter
    @Setter
    private Long playerId;

    @Column(name = "old_team_id")
    @Getter
    @Setter
    private Long oldTeamId;

    @Column(name = "new_team_id")
    @Getter
    @Setter
    private Long newTeamId;

    @Column(name = "contract_fee")
    @Getter
    @Setter
    private Double contractFee;

    public Transfer(Timestamp createdAt, Long playerId, Long oldTeamId, Long newTeamId, Double contractFee) {
        this.createdAt = createdAt;
        this.playerId = playerId;
        this.oldTeamId = oldTeamId;
        this.newTeamId = newTeamId;
        this.contractFee = contractFee;
    }
}
