package net.troja.application.servicea.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ServiceBClient {
    private RestTemplate restTemplate = new RestTemplate();

    public boolean create(final Metadata data) {
        final ResponseEntity<Metadata> responseEntity = restTemplate.postForEntity("http://localhost:8081/metadata", data, Metadata.class);
        return responseEntity.getStatusCode().is2xxSuccessful();
    }

    public Metadata findByUniqueId(final String uniqueId) {
        final ResponseEntity<Metadata> responseEntity = restTemplate.getForEntity("http://localhost:8081/metadata?uniqueid=" + uniqueId, Metadata.class);
        if (responseEntity.getStatusCode().equals(HttpStatus.FOUND)) {
            return responseEntity.getBody();
        } else {
            return null;
        }
    }

    public void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
