package net.troja.application.serviceb.rest;

import java.util.regex.Pattern;

import net.troja.application.serviceb.data.MetadataRepository;
import net.troja.application.serviceb.model.Metadata;

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
@RequestMapping("metadata")
public class MetadataController {
    private static final Logger LOGGER = LogManager.getLogger(MetadataController.class);
    private static final Pattern PATTERN = Pattern.compile("^[0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!#$%&()\\[\\]\\{\\}*+-._/@~|,;:]{20}$");
    @Autowired
    private MetadataRepository repository;

    @PostMapping
    public ResponseEntity<Metadata> create(@RequestBody final Metadata metadata) {
        ResponseEntity<Metadata> result = null;
        if (isValidUniqueId(metadata.getUniqueId())) {
            final Metadata savedData = repository.save(metadata);
            result = new ResponseEntity<Metadata>(savedData, HttpStatus.CREATED);
        } else {
            result = new ResponseEntity<Metadata>(HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("Create " + metadata.toString() + " -- " + result.getStatusCodeValue());
        return result;
    }

    @GetMapping()
    public ResponseEntity<Metadata> find(@RequestParam("uniqueid") final String uniqueId) {
        ResponseEntity<Metadata> result = null;
        if (isValidUniqueId(uniqueId)) {
            final Metadata metadata = repository.findByUniqueId(uniqueId);
            if (metadata != null) {
                result = new ResponseEntity<Metadata>(metadata, HttpStatus.FOUND);
            } else {
                result = new ResponseEntity<Metadata>(HttpStatus.NOT_FOUND);
            }
        } else {
            result = new ResponseEntity<Metadata>(HttpStatus.BAD_REQUEST);
        }
        LOGGER.info("Find " + uniqueId + " -- " + result.getStatusCodeValue());
        return result;
    }

    public static boolean isValidUniqueId(final String uniqueId) {
        return (uniqueId != null) && PATTERN.matcher(uniqueId).matches();
    }
}
