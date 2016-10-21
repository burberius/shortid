package net.troja.application.serviceb.data;

import net.troja.application.serviceb.model.Metadata;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends CrudRepository<Metadata, Long> {
    Metadata findByUniqueId(String uniqueId);
}
