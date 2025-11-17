package com.fime.ratemyprofs.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "votetypes")
public class VoteType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "votetypeid")
    private Integer voteTypeId;

    @Column(name = "votename", nullable = false, unique = true, length = 30)
    private String voteName;

    @Column(name = "votevalue", nullable = false)
    private Integer voteValue;
}
