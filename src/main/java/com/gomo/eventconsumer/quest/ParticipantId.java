package com.gomo.eventconsumer.quest;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class ParticipantId implements Serializable {

    private UUID id;

    protected ParticipantId() {
    }

    private ParticipantId(UUID id) {
        this.id = id;
    }

    public static ParticipantId of(UUID id) {
        return new ParticipantId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ParticipantId participantId = (ParticipantId)o;
        return Objects.equals(id, participantId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
