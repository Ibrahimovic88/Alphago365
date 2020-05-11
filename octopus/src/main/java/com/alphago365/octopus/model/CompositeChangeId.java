package com.alphago365.octopus.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

@EqualsAndHashCode
@Embeddable
@Data
@NoArgsConstructor
public class CompositeChangeId implements Serializable {

    private Long matchId;

    private Integer providerId;

    @NotNull
    private Instant updateTime;

    public CompositeChangeId(Long matchId, Integer providerId, @NotNull Instant updateTime) {
        this.matchId = matchId;
        this.providerId = providerId;
        this.updateTime = updateTime;
    }
}