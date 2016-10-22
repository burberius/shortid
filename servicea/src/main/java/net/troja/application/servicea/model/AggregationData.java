package net.troja.application.servicea.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class AggregationData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private long id;
    private UUID uuid;
    private String aggregate;

    public AggregationData() {
        super();
    }

    public AggregationData(final UUID uuid, final String aggregate) {
        super();
        this.uuid = uuid;
        this.aggregate = aggregate;
    }

    public long getId() {
        return id;
    }

    public void setId(final long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID uuid) {
        this.uuid = uuid;
    }

    public String getAggregate() {
        return aggregate;
    }

    public void setAggregate(final String aggregate) {
        this.aggregate = aggregate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((aggregate == null) ? 0 : aggregate.hashCode());
        result = (prime * result) + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AggregationData other = (AggregationData) obj;
        if (aggregate == null) {
            if (other.aggregate != null) {
                return false;
            }
        } else if (!aggregate.equals(other.aggregate)) {
            return false;
        }
        if (uuid == null) {
            if (other.uuid != null) {
                return false;
            }
        } else if (!uuid.equals(other.uuid)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AggregationData [uuid=" + uuid + ", aggregate=" + aggregate + "]";
    }
}
