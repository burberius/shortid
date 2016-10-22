package net.troja.application.servicea.rest;

import java.util.UUID;

import net.troja.application.servicea.IdConversionService;
import net.troja.application.servicea.data.AggregationDataRepository;
import net.troja.application.servicea.model.AggregationData;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("aggregate")
public class AggregateController {
    private static final Logger LOGGER = LogManager.getLogger(AggregateController.class);
    @Autowired
    private AggregationDataRepository repository;
    @Autowired
    private ServiceBClient client;
    @Autowired
    private IdConversionService idService;

    @PostMapping
    public ResponseEntity<AggregationData> create(@RequestBody final AggregationData data) {
        if (data.getUuid() == null) {
            data.setUuid(UUID.randomUUID());
        }
        final AggregationData saved = repository.save(data);
        final Metadata metadata = new Metadata(idService.convert(saved.getUuid()), Integer.toHexString(saved.hashCode()));
        ResponseEntity<AggregationData> result = null;
        if (client.create(metadata)) {
            result = new ResponseEntity<AggregationData>(saved, HttpStatus.CREATED);
        } else {
            result = new ResponseEntity<AggregationData>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        LOGGER.info("Create " + data + " -- " + result.getStatusCodeValue());
        return result;
    }

    @GetMapping
    public ResponseEntity<AggregationData> findByUuid(@RequestParam("uuid") final String uuid) {
        final AggregationData aggregationData = repository.findByUuid(UUID.fromString(uuid));
        ResponseEntity<AggregationData> result = null;
        if (aggregationData != null) {
            final Metadata metadata = client.findByUniqueId(idService.convert(aggregationData.getUuid()));
            if ((metadata != null) && metadata.getContent().equals(Integer.toHexString(aggregationData.hashCode()))) {
                result = new ResponseEntity<AggregationData>(aggregationData, HttpStatus.FOUND);
            } else {
                result = new ResponseEntity<AggregationData>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            result = new ResponseEntity<AggregationData>(HttpStatus.NOT_FOUND);
        }
        LOGGER.info("Create " + uuid + " -- " + result.getStatusCodeValue());
        return result;
    }

    public void setClient(final ServiceBClient client) {
        this.client = client;
    }
}
