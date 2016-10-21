package net.troja.application.serviceb.rest;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import net.troja.application.serviceb.data.MetadataRepository;
import net.troja.application.serviceb.model.Metadata;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MetadataControllerTest {
    private static final String VALID_ID = "0o,[_+2WS@0o,[_+2WS@";

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected MetadataRepository repository;

    @Before
    public void setUp() {
        repository.deleteAll();
    }

    @Test
    public void isValidUniqueId() {
        assertThat(MetadataController.isValidUniqueId(null), equalTo(false));
        assertThat(MetadataController.isValidUniqueId("asd"), equalTo(false));
        assertThat(MetadataController.isValidUniqueId(VALID_ID), equalTo(true));
        assertThat(MetadataController.isValidUniqueId("4A#v(62f+ 2§aa)asd[:"), equalTo(false));
    }

    @Test
    public void createSuccess() {
        assertThat(repository.count(), equalTo(0L));
        final Metadata metadata = new Metadata(VALID_ID, "Nothing here");

        final ResponseEntity<Metadata> responseEntity = restTemplate.postForEntity("/metadata", metadata, Metadata.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.CREATED));
        assertThat(repository.count(), equalTo(1L));
    }

    @Test
    public void createError() {
        assertThat(repository.count(), equalTo(0L));
        final Metadata metadata = new Metadata("21458asdf4g-#dfe4", "Nothing here");

        final ResponseEntity<Metadata> responseEntity = restTemplate.postForEntity("/metadata", metadata, Metadata.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
        assertThat(repository.count(), equalTo(0L));
    }

    @Test
    public void findSuccess() {
        final Metadata metadata = new Metadata(VALID_ID, "Nothing here");
        final Metadata saved = repository.save(metadata);

        final ResponseEntity<Metadata> responseEntity = restTemplate.getForEntity("/metadata?uniqueid=" + VALID_ID, Metadata.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.FOUND));
        assertThat(responseEntity.getBody(), equalTo(saved));
    }

    @Test
    public void findInvalidId() {
        final ResponseEntity<Metadata> responseEntity = restTemplate.getForEntity("/metadata?uniqueid=haha", Metadata.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void findNot() {
        final Metadata metadata = new Metadata("::::::::::::::::::::", "Nothing here");
        repository.save(metadata);

        final ResponseEntity<Metadata> responseEntity = restTemplate.getForEntity("/metadata?uniqueid=" + VALID_ID, Metadata.class);

        assertThat(responseEntity.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }
}
