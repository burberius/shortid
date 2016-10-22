package net.troja.application.servicea.data;

import java.util.UUID;

import net.troja.application.servicea.model.AggregationData;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregationDataRepository extends CrudRepository<AggregationData, Long> {
    AggregationData findByUuid(UUID uuid);
}
